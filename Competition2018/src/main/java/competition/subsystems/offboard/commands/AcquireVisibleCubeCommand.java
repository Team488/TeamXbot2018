package competition.subsystems.offboard.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;

public class AcquireVisibleCubeCommand extends OffboardProcessingWithDriveCommand {

    @Inject
    protected AcquireVisibleCubeCommand(OffboardInterfaceSubsystem offboardSubsystem, DriveSubsystem driveSubsystem) {
        super(0x01, offboardSubsystem, driveSubsystem);
    }
    
    @Override
    protected void handleIncomingNonDrivePacket(OffboardCommunicationPacket packet) {
        // Intentionally left blank; nothing to do

    }
}
