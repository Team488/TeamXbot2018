package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.MoveElevatorToHeightAndStabilizeCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.commands.WristUpCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoPutCubeOnSwitchCommandGroup extends CommandGroup {
    
    public DriveForDistanceCommand driveToDistance;
    public MoveElevatorToHeightAndStabilizeCommand elevatorToSwitch;
    
    @Inject
    public AutoPutCubeOnSwitchCommandGroup(
            DriveForDistanceCommand driveToDistance,
            ElevatorSubsystem elevator,
            GripperEjectCommand delivery,
            MoveElevatorToHeightAndStabilizeCommand elevatorToSwitch,
            WristUpCommand wristUp) {
        elevatorToSwitch.setTargetHeight(elevator.getTargetSwitchDropHeight());
        /**
         * 81.5 is the distance the robot with cube,
         * to the switch in inches
         */
        driveToDistance.setDeltaDistance(81.5);
        
        this.driveToDistance = driveToDistance;
        this.elevatorToSwitch = elevatorToSwitch;
        
        this.addParallel(elevatorToSwitch);
        this.addParallel(driveToDistance);
        this.addSequential(wristUp);
        this.addSequential(delivery);
    }

}
