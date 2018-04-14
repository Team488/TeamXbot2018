package competition.subsystems.offboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.offboard.data.TargetCubeInfo;
import competition.subsystems.offboard.packets.TargetCubePacket;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.logic.WatchdogTimer;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class OffboardInterfaceSubsystem extends BaseSubsystem implements PeriodicDataSource {
    private static Logger log = Logger.getLogger(OffboardInterfaceSubsystem.class);
    
    public static final double CM_PER_INCH = 2.54;
    public static final double METERS_PER_INCH = 0.0254;
    
    private final Queue<OffboardCommunicationPacket> incomingPacketQueue = new LinkedList<>();
    private static final int PACKET_QUEUE_MAX_LENGTH = 25;
    
    private final DriveSubsystem driveSubsystem;
    private final XOffboardCommsInterface rawCommsInterface;

    private Double lastWheelOdomSend = null;
    private double lastLeftDriveDistance;
    private double lastRightDriveDistance;
    
    private TargetCubeInfo targetCube = null;
    private BooleanProperty hasTargetCubeProp;
    private DoubleProperty targetCubeXProp;
    private DoubleProperty targetCubeYProp;
    private DoubleProperty targetCubeZProp;

    private WatchdogTimer connectionWatchdog;
    private WatchdogTimer cubeLocationWatchdog;
    
    @Inject
    public OffboardInterfaceSubsystem(
                XPropertyManager propManager,
                DriveSubsystem driveSubsystem,
                PoseSubsystem poseSubsystem,
                XOffboardCommsInterface commsInterface) {
        log.info("Creating");
        
        this.driveSubsystem = driveSubsystem;
        this.rawCommsInterface = commsInterface;
        
        hasTargetCubeProp = propManager.createEphemeralProperty(getPrefix()+"Target Cube/Has target?", false);
        targetCubeXProp = propManager.createEphemeralProperty(getPrefix()+"Target Cube/X", 0);
        targetCubeYProp = propManager.createEphemeralProperty(getPrefix()+"Target Cube/Y", 0);
        targetCubeZProp = propManager.createEphemeralProperty(getPrefix()+"Target Cube/Z", 0);
        
        connectionWatchdog = new WatchdogTimer(
            3.0,
            () -> log.info("Connected"),
            () -> {
                log.info("Disconnected");
                this.targetCube = null;
                updateProps();
        });
        
        cubeLocationWatchdog = new WatchdogTimer(
            0.3,
            null,
            () -> {
                this.targetCube = null;
                updateProps();
        });
    }
    
    private void sendWheelOdomUpdate() {
        final double timestamp = Timer.getFPGATimestamp();
        
        double leftDriveDistanceInches = this.driveSubsystem.getLeftTotalDistance();
        double rightDriveDistanceInches = this.driveSubsystem.getRightTotalDistance();

        if (lastWheelOdomSend == null) {
            lastWheelOdomSend = timestamp;
            lastLeftDriveDistance = leftDriveDistanceInches;
            lastRightDriveDistance = rightDriveDistanceInches;
        }
        
        double leftDriveDeltaCm = (leftDriveDistanceInches - lastLeftDriveDistance) * CM_PER_INCH;
        double rightDriveDeltaCm = (rightDriveDistanceInches - lastRightDriveDistance) * CM_PER_INCH;

        this.lastLeftDriveDistance = leftDriveDistanceInches;
        this.lastRightDriveDistance = rightDriveDistanceInches;
        
        double timeDelta = timestamp - lastWheelOdomSend;
        lastWheelOdomSend = timestamp;
        
        rawCommsInterface.sendRaw(
                OffboardCommsConstants.PACKET_TYPE_WHEEL_ODOM,
                OffboardFramePackingUtils.packWheelOdomFrame(leftDriveDeltaCm, rightDriveDeltaCm, timeDelta));
    }
    
    public void sendSetCurrentCommand(int commandId) {
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_SET_CURRENT_COMMAND, OffboardFramePackingUtils.packSetCommandFrame(commandId));
    }
    
    public Collection<OffboardCommunicationPacket> getPacketQueue() {
        ArrayList<OffboardCommunicationPacket> queueContents = new ArrayList<>();
        for(int i = 0; i < this.incomingPacketQueue.size(); i++) {
            queueContents.add(this.incomingPacketQueue.remove());
        }
        return queueContents;
    }
    
    public void clearPacketQueue() {
        this.incomingPacketQueue.clear();
    }
    
    private boolean handlePacketIfPossible(OffboardCommunicationPacket packet) {
        if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_DETECTED_CUBE) {
            try {
                TargetCubePacket cubePacket = TargetCubePacket.parse(packet.data);
                this.targetCube = cubePacket.targetInfo;
                cubeLocationWatchdog.kick();
                updateProps();
            }
            catch (IllegalArgumentException e) {
                log.warn("Detected cube packet failed to parse");
            }
            return true;
        }
        
        return false;
    }
    
    private void updateProps() {
        this.hasTargetCubeProp.set(this.targetCube != null);
        this.targetCubeXProp.set(this.targetCube == null ? 0 : this.targetCube.xInches);
        this.targetCubeYProp.set(this.targetCube == null ? 0 : this.targetCube.yInches);
        this.targetCubeZProp.set(this.targetCube == null ? 0 : this.targetCube.zInches);
    }
    
    public TargetCubeInfo getTargetCube() {
        return this.targetCube;
    }

    @Override
    public void updatePeriodicData() {
        sendWheelOdomUpdate();

        // TODO: Tune loop? Logging when hit limit?
        int numPacketsDropped = 0;
        for(int receiveCount = 0; receiveCount < 5; receiveCount++) {
            OffboardCommunicationPacket packet = this.rawCommsInterface.receiveRaw(OffboardCommsConstants.SENDER_ID_JETSON);
            if(packet == null) {
                break;
            }
            
            connectionWatchdog.kick();
            
            if(handlePacketIfPossible(packet)) {
                continue;
            }
            
            this.incomingPacketQueue.add(packet);
            if (this.incomingPacketQueue.size() > PACKET_QUEUE_MAX_LENGTH) {
                this.incomingPacketQueue.remove();
                numPacketsDropped++;
            }
        }
        
        if (numPacketsDropped > 0 && this.getCurrentCommand() != null) {
            // TODO: this.getCurrentCommand() instanceof OffboardProcessingCommand
            log.warn(numPacketsDropped + " offboard comms packets dropped from queue while command is running;"
                    + " all commands running on the offboard subsystem should process incoming packets.");
        }
        
        connectionWatchdog.check();
        cubeLocationWatchdog.check();
    }
}
