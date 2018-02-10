package competition.subsystems.offboard;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.Timer;
import openrio.powerup.MatchData;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.properties.XPropertyManager;

@Singleton
public class OffboardInterfaceSubsystem extends BaseSubsystem implements PeriodicDataSource {
    private static Logger log = Logger.getLogger(OffboardInterfaceSubsystem.class);

    public static final double CM_PER_INCH = 2.54;
    public static final double METERS_PER_INCH = 0.0254;
    
    private final Queue<OffboardCommunicationPacket> incomingPacketQueue = new LinkedList<>();
    private final int PACKET_QUEUE_MAX_LENGTH = 25;
    
    private final DriveSubsystem driveSubsystem;
    private final PoseSubsystem poseSubsystem;
    private final XOffboardCommsInterface rawCommsInterface;

    private Double lastWheelOdomSend = null;
    private double lastLeftDriveDistance;
    private double lastRightDriveDistance;
    
    @Inject
    public OffboardInterfaceSubsystem(XPropertyManager propManager, DriveSubsystem driveSubsystem, 
    		PoseSubsystem poseSubsystem, XOffboardCommsInterface commsInterface) {
    	log.info("Creating");
        
        this.driveSubsystem = driveSubsystem;
        this.poseSubsystem = poseSubsystem;
        this.rawCommsInterface = commsInterface;
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
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_SET_CURRENT_COMMAND, 
        		OffboardFramePackingUtils.packSetCommandFrame(commandId));
    }
    
    public Collection<OffboardCommunicationPacket> getPacketQueue() {
        ArrayList<OffboardCommunicationPacket> queueContents = new ArrayList<>();
        for(int i = 0; i < this.incomingPacketQueue.size(); i++) {
            queueContents.add(this.incomingPacketQueue.remove());
        }
        return queueContents;
    }
    
    private void sendScoringPlacement() {
    	//Sequence - Near, Scale, Far
        //Left - 1, Right - 0
        ArrayList<Integer> sequence = new ArrayList<Integer>();
        for(MatchData.GameFeature feature: MatchData.GameFeature.values()) {
        	if(MatchData.getOwnedSide(feature).equals(MatchData.OwnedSide.LEFT)) {
        		sequence.add(0);
        	}
        	else if(MatchData.getOwnedSide(feature).equals(MatchData.OwnedSide.RIGHT)) {
        		sequence.add(1);
        	}
        	else {
        		//Insert Error Message as it is unknown
        	}
        }
        int total = 0;
        for (Integer i : sequence) { 
            total = 10*total + i;
        }
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_SCORING_PLACEMENT,
        		OffboardFramePackingUtils.packScoringPlacement(total));
    }
    
    public void clearPacketQueue() {
        this.incomingPacketQueue.clear();
    }
    
    @Override
    public void updatePeriodicData() {
        sendWheelOdomUpdate();
        sendScoringPlacement();

        // TODO: Tune loop? Logging when hit limit?
        int numPacketsDropped = 0;
        for(int receiveCount = 0; receiveCount < 5; receiveCount++) {
            OffboardCommunicationPacket packet = this.rawCommsInterface.receiveRaw();
            if(packet == null) {
                break;
            }
            
            this.incomingPacketQueue.add(packet);
            if (this.incomingPacketQueue.size() > PACKET_QUEUE_MAX_LENGTH) {
                this.incomingPacketQueue.remove();
                numPacketsDropped++;
            }
        }
        
        if (numPacketsDropped > 0 && this.getCurrentCommand() != null) {
            // TODO: this.getCurrentCommand() instanceof OffboardProcessingCommand
            log.warn(numPacketsDropped 
            		+ " offboard comms packets dropped from queue while command is running;"
            		+ " all commands running on the offboard subsystem should process incoming packets.");
        }
    }
}
