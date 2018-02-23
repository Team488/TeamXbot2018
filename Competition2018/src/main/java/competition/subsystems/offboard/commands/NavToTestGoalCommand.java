package competition.subsystems.offboard.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;

public class NavToTestGoalCommand extends OffboardProcessingWithDriveCommand {

    @Inject
    protected NavToTestGoalCommand(OffboardInterfaceSubsystem offboardSubsystem, DriveSubsystem driveSubsystem) {
        super(0x02, offboardSubsystem, driveSubsystem);
    }
    
    @Override
    protected void handleIncomingNonDrivePacket(OffboardCommunicationPacket packet) {
        // Intentionally left blank; nothing to do
    }
}
