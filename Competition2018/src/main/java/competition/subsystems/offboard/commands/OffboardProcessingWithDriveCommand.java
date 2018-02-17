package competition.subsystems.offboard.commands;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.offboard.OffboardCommsConstants;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.packets.DrivePowerCommandPacket;
import competition.subsystems.offboard.packets.DriveVelCommandPacket;

public abstract class OffboardProcessingWithDriveCommand extends OffboardProcessingCommand {
    private final DriveSubsystem driveSubsystem;
    
    protected OffboardProcessingWithDriveCommand(int commandId, OffboardInterfaceSubsystem offboardSubsystem, DriveSubsystem driveSubsystem) {
        super(commandId, offboardSubsystem);
        this.driveSubsystem = driveSubsystem;
        this.requires(driveSubsystem);
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
            DrivePowerCommandPacket drivePacket = DrivePowerCommandPacket.parse(packet.data);
            if (drivePacket.commandId == this.commandId) {
                // TODO: Log occasionally
                this.driveSubsystem.drive(drivePacket.leftPower, drivePacket.rightPower);
            }
            else {
                log.warn("Received \"drive power command\" packet for command which is not currently running");
            }
        }
        else if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_DRIVE_VEL_COMMAND) {
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
        else {
            handleIncomingNonDrivePacket(packet);
        }
    }
}
