package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.MoveElevatorToHeightAndStabilizeCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPutCubeOnSwitchCommandGroup extends CommandGroup {
    
    @Inject
    public AutoPutCubeOnSwitchCommandGroup(
            DriveForDistanceCommand driveToDistance,
            ElevatorSubsystem elevator,
            GripperEjectCommand delivery,
            MoveElevatorToHeightAndStabilizeCommand elevatorSet,
            SetWristAngleCommand wristSet) {
        elevatorSet.setTargetHeight(elevator.getTargetSwitchDropHeight());
        /**
         * 81.5 is the distance the robot with cube,
         * to the switch in inches
         */
        driveToDistance.setDeltaDistance(81.5);
        wristSet.setGoalAngle(0);
        
        this.addParallel(elevatorSet, 1);
        this.addParallel(driveToDistance);
        this.addSequential(wristSet, 1);
        this.addSequential(delivery);
    }

}
