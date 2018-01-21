package competition.subsystems.offboard.commands;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.offboard.OffboardCommsConstants;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.packets.DriveCommandPacket;

public abstract class OffboardProcessingWithDriveCommand extends OffboardProcessingCommand {
    private final DriveSubsystem driveSubsystem;
    
    protected OffboardProcessingWithDriveCommand(int commandId, OffboardInterfaceSubsystem offboardSubsystem, DriveSubsystem driveSubsystem) {
        super(commandId, offboardSubsystem);
        this.driveSubsystem = driveSubsystem;
        this.requires(driveSubsystem);
    }
    
    protected abstract void handleIncomingNonDrivePacket(OffboardCommunicationPacket packet);
    
    @Override
    protected void handleIncomingPacket(OffboardCommunicationPacket packet) {
        if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_DRIVE_COMMAND) {
            DriveCommandPacket drivePacket = DriveCommandPacket.parse(packet.data);
            if (drivePacket.commandId == this.commandId) {
                // TODO: Log occasionally
                this.driveSubsystem.drive(drivePacket.leftPower, drivePacket.rightPower);
            }
            else {
                log.warn("Received \"drive command\" packet for command which is not currently running");
            }
        }
        else {
            handleIncomingNonDrivePacket(packet);
        }
    }
}
