package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.MoveElevatorToHeightAndStabilizeCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.commands.WristUpCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPutCubeOnScaleCommandGroup extends CommandGroup {
    
    @Inject
    public AutoPutCubeOnScaleCommandGroup(
            DriveForDistanceCommand driveToDistance,
            ElevatorSubsystem elevator,
            GripperEjectCommand delievery,
            MoveElevatorToHeightAndStabilizeCommand elevatorToScale,
            WristUpCommand wristUp) {
        elevator.setTargetHeight(elevator.getTargetScaleHighHeight());
        /**
         * 243 is the distance the robot is with the cube,
         * to the scale in inches
         */
        driveToDistance.setDeltaDistance(243);
        
        this.addParallel(elevatorToScale);
        this.addParallel(driveToDistance);
        this.addSequential(wristUp);
        this.addSequential(delievery); 
    }

}
