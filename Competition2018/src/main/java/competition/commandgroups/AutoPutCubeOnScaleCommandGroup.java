package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.MoveElevatorToHeightAndStabilizeCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPutCubeOnScaleCommandGroup extends CommandGroup {
    
    public SetWristAngleCommand wristSet;
    
    @Inject
    public AutoPutCubeOnScaleCommandGroup(
            DriveForDistanceCommand driveToDistance,
            ElevatorSubsystem elevator,
            SetWristAngleCommand wristSet,
            GripperEjectCommand delivery,
            MoveElevatorToHeightAndStabilizeCommand elevatorToScale) {
        elevatorToScale.setTargetHeight(elevator.getTargetScaleHighHeight());
        /**
         * 243 is the distance the robot is with the cube,
         * to the scale in inches
         */
        driveToDistance.setDeltaDistance(243);
        wristSet.setGoalAngle(0);
        
        this.wristSet = wristSet;
        
        this.addParallel(driveToDistance);
        this.addParallel(elevatorToScale);
        this.addSequential(wristSet);
        this.addSequential(delivery); 
    }

}
