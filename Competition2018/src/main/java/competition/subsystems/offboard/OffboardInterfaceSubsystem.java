package competition.subsystems.offboard;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.properties.XPropertyManager;

@Singleton
public class OffboardInterfaceSubsystem extends BaseSubsystem implements PeriodicDataSource {
    private static Logger log = Logger.getLogger(OffboardInterfaceSubsystem.class);

    private static final double INCHES_TO_CM = 2.54;
    
    private final DriveSubsystem driveSubsystem;
    private final PoseSubsystem poseSubsystem;
    public final XOffboardCommsInterface rawCommsInterface;

    private Double lastWheelOdomSend = null;
    private double lastLeftDriveDistance;
    private double lastRightDriveDistance;
    
    @Inject
    public OffboardInterfaceSubsystem(XPropertyManager propManager, DriveSubsystem driveSubsystem, PoseSubsystem poseSubsystem, XOffboardCommsInterface commsInterface) {
        log.info("Creating");
        
        this.driveSubsystem = driveSubsystem;
        this.poseSubsystem = poseSubsystem;
        this.rawCommsInterface = commsInterface;
    }
    
    private void sendWheelOdomUpdate() {
        final double timestamp = Timer.getFPGATimestamp();
        
        double leftDriveDistanceInches = this.driveSubsystem.getLeftTotalDistance();
        double rightDriveDistanceInches = this.driveSubsystem.getRightTotalDistance();
        
        double leftDriveDeltaCm = (leftDriveDistanceInches - lastLeftDriveDistance) * INCHES_TO_CM;
        double rightDriveDeltaCm = (rightDriveDistanceInches - lastRightDriveDistance) * INCHES_TO_CM;
        
        if (lastWheelOdomSend == null) {
            lastWheelOdomSend = timestamp;
        }
        double timeDelta = timestamp - lastWheelOdomSend;
        lastWheelOdomSend = timestamp;
        
        rawCommsInterface.sendRaw(
                OffboardCommsConstants.PACKET_TYPE_WHEEL_ODOM,
                OffboardFramePackingUtils.packWheelOdomFrame(leftDriveDeltaCm, rightDriveDeltaCm, timeDelta));
    }
    
    private void sendOrientationUpdate() {
        // TODO: Port quaternion code
        /*Quaternion orientation = poseSubsystem.getImuOrientationQuaternion();
        rawCommsInterface.sendRaw(
                OffboardCommsConstants.PACKET_TYPE_ORIENTATION,
                OffboardFramePackingUtils.packOrientationFrame(orientation.w, orientation.x, orientation.y, orientation.z));*/
    }
    
    private void sendHeadingUpdate() {
        double heading = poseSubsystem.getCurrentHeading().getValue();
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_HEADING, OffboardFramePackingUtils.packHeadingFrame(heading));
    }
    
    public void sendSetCurrentCommand(int commandId) {
        rawCommsInterface.sendRaw(OffboardCommsConstants.PACKET_TYPE_SET_CURRENT_COMMAND, OffboardFramePackingUtils.packSetCommandFrame(commandId));
    }

    @Override
    public void updatePeriodicData() {
        sendWheelOdomUpdate();
        sendOrientationUpdate();
        sendHeadingUpdate();
    }
}
