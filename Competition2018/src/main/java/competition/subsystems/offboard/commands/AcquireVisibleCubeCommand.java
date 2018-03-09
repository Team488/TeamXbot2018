package competition.subsystems.offboard.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.wrist.WristSubsystem;

public class AcquireVisibleCubeCommand extends OffboardProcessingWithRobotControlCommand {

    @Inject
    protected AcquireVisibleCubeCommand(OffboardInterfaceSubsystem offboardSubsystem, DriveSubsystem driveSubsystem,
            ElevatorSubsystem elevatorSubystem, WristSubsystem wristSubsystem,
            GripperIntakeSubsystem gripperIntakeSubsystem) {
        super(0x01, offboardSubsystem, driveSubsystem, elevatorSubystem, wristSubsystem, gripperIntakeSubsystem);
    }

    @Override
    protected void handleIncomingNonDrivePacket(OffboardCommunicationPacket packet) {
        // Intentionally left blank; nothing to do
    }
}
