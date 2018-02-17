package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPutCubeOnSwitchCommandGroup extends CommandGroup {
    
    @Inject
    public AutoPutCubeOnSwitchCommandGroup(
            DriveForDistanceCommand driveToDistance,
            ElevatorSubsystem elevator,
            SetElevatorTargetHeightCommand moveToSwitchHeight,
            GripperEjectCommand deliever
            ) {
        moveToSwitchHeight.setGoalHeight(elevator.getTargetSwitchDropHeight());
        /**
         * 81.5 is the distance the robot with cube,
         * to the switch in inches
         */
        driveToDistance.setDeltaDistance(81.5);
        
        this.addParallel(moveToSwitchHeight);
        this.addParallel(driveToDistance);
        this.addSequential(deliever);
    }

}
