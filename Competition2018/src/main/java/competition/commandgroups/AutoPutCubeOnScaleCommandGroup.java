package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.commands.WristDownCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPutCubeOnScaleCommandGroup extends CommandGroup {
    
    @Inject
    public AutoPutCubeOnScaleCommandGroup(
            DriveForDistanceCommand driveToDistance,
            ElevatorSubsystem elevator,
            WristDownCommand wristDown,
            GripperEjectCommand delivery,
            SetElevatorTargetHeightCommand elevatorSet) {
        elevatorSet.setGoalHeight(elevator.getTargetScaleHighHeight());
        /**
         * 243 is the distance the robot is with the cube,
         * to the scale in inches
         */
        driveToDistance.setDeltaDistance(243);
        
        this.addParallel(elevatorSet, 1);
        this.addParallel(driveToDistance);
        this.addSequential(wristDown, 1);
        this.addSequential(delivery); 
    }

}
