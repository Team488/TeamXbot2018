package competition.subsystems.offboard.commands;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import competition.subsystems.offboard.OffboardCommsConstants;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.packets.DrivePowerCommandPacket;
import competition.subsystems.offboard.packets.DriveVelCommandPacket;
import competition.subsystems.offboard.packets.ElevatorPositionCommandPacket;
import competition.subsystems.offboard.packets.GripperCommandPacket;
import competition.subsystems.offboard.packets.WristCommandPacket;
import competition.subsystems.wrist.WristSubsystem;

public abstract class OffboardProcessingWithRobotControlCommand extends OffboardProcessingCommand {
    
    private final DriveSubsystem driveSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;
    private final WristSubsystem wristSubsystem;
    private final GripperIntakeSubsystem gripperIntakeSubsystem;

    protected OffboardProcessingWithRobotControlCommand(int commandId, 
            OffboardInterfaceSubsystem offboardSubsystem, 
            DriveSubsystem driveSubsystem,
            ElevatorSubsystem elevatorSubsystem,
            WristSubsystem wristSubsystem,
            GripperIntakeSubsystem gripperIntakeSubsystem) {
        super(commandId, offboardSubsystem);
        this.driveSubsystem = driveSubsystem;
        this.requires(driveSubsystem);
        this.elevatorSubsystem = elevatorSubsystem;
        this.requires(elevatorSubsystem);
        this.wristSubsystem = wristSubsystem;
        this.requires(wristSubsystem);
        this.gripperIntakeSubsystem = gripperIntakeSubsystem;
        this.requires(gripperIntakeSubsystem);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        this.driveSubsystem.resetVelocityAccum();
    }
    
    protected abstract void handleIncomingNonDrivePacket(OffboardCommunicationPacket packet);
    
    @Override
    protected void handleIncomingPacket(OffboardCommunicationPacket packet) {
        if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_DRIVE_POWER_COMMAND) {
            try {
                DrivePowerCommandPacket drivePacket = DrivePowerCommandPacket.parse(packet.data);
                if (drivePacket.commandId == this.commandId) {
                    // TODO: Log occasionally
                    this.driveSubsystem.drive(drivePacket.leftPower, drivePacket.rightPower);
                }
                else {
                    log.warn("Received \"drive power command\" packet for command which is not currently running");
                }
            }
            catch (IllegalArgumentException e) {
                log.warn("Drive power command packet failed to parse");
            }
        }
        else if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_DRIVE_VEL_COMMAND) {
            try {
                DriveVelCommandPacket drivePacket = DriveVelCommandPacket.parse(packet.data);
                if (drivePacket.commandId == this.commandId) {
                    // TODO: Log occasionally
                    double leftVelocityInchesPerSecond = drivePacket.leftVelocityMetersPerSecond / OffboardInterfaceSubsystem.METERS_PER_INCH;
                    double rightVelocityInchesPerSecond = drivePacket.rightVelocityMetersPerSecond / OffboardInterfaceSubsystem.METERS_PER_INCH;
                    this.driveSubsystem.driveTankVelocity(leftVelocityInchesPerSecond, rightVelocityInchesPerSecond);
                }
                else {
                    log.warn("Received \"drive velocity command\" packet for command which is not currently running");
                }
            }
            catch (IllegalArgumentException e) {
                log.warn("Drive velocity command packet failed to parse");
            }
        }
        else if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_ELEVATOR_POSITION_COMMAND) {
            ElevatorPositionCommandPacket elevatorPacket = ElevatorPositionCommandPacket.parse(packet.data);
            if (elevatorPacket.commandId == this.commandId) {
                // TODO: Log occasionally
                this.elevatorSubsystem.setTargetHeight(elevatorPacket.elevatorGoal);
            }
            else {
                log.warn("Received \"set elevator position command\" packet for command which is not currently running");
            }
        }
        else if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_WRIST_COMMAND) {
            try {
                WristCommandPacket wristCommandPacket = WristCommandPacket.parse(packet.data);
                if (wristCommandPacket.commandId == this.commandId) {
                    // TODO: Log occasionally
                    this.wristSubsystem.setTargetAngle(wristCommandPacket.position);
                }
                else {
                    log.warn("Received \"wrist command\" packet for command which is not currently running");
                }
            }
            catch (IllegalArgumentException e) {
                log.warn("Wrist command packet failed to parse");
            }
        }
        else if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_GRIPPER_CONTROL_COMMAND) {
            try {
                GripperCommandPacket gripperCommandPacket = GripperCommandPacket.parse(packet.data);
                if (gripperCommandPacket.commandId == this.commandId) {
                    // TODO: Log occasionally
                    this.gripperIntakeSubsystem.setIntakeMode(gripperCommandPacket.gripperMode);
                }
                else {
                    log.warn("Received \"gripper intake command\" packet for command which is not currently running");
                }
            }
            catch (IllegalArgumentException e) {
                log.warn("Gripper command packet failed to parse");
            }
        }
        else {
            handleIncomingNonDrivePacket(packet);
        }
    }
}
