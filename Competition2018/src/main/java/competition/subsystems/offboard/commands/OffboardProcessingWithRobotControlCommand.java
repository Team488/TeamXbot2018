package competition.subsystems.offboard.commands;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.offboard.OffboardCommsConstants;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.packets.DrivePowerCommandPacket;
import competition.subsystems.offboard.packets.DriveVelCommandPacket;
import competition.subsystems.offboard.packets.ElevatorPositionCommandPacket;

public abstract class OffboardProcessingWithRobotControlCommand extends OffboardProcessingCommand {
    
    private final DriveSubsystem driveSubsystem;
    private final ElevatorSubsystem elevatorSubsystem;

    protected OffboardProcessingWithRobotControlCommand(int commandId, 
            OffboardInterfaceSubsystem offboardSubsystem, 
            DriveSubsystem driveSubsystem,
            ElevatorSubsystem elevatorSubsystem) {
        super(commandId, offboardSubsystem);
        this.driveSubsystem = driveSubsystem;
        this.requires(driveSubsystem);
        this.elevatorSubsystem = elevatorSubsystem;
        this.requires(elevatorSubsystem);
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
        else {
            handleIncomingNonDrivePacket(packet);
        }
    }
}
