package competition.commandgroups;

import com.google.inject.Inject;

import edu.wpi.first.wpilibj.command.CommandGroup;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.wrist.commands.WristDownCommand;

public class CollectCubeCommandGroup extends CommandGroup{
    
    @Inject
    public CollectCubeCommandGroup(
            ElevatorSubsystem elevator,
            SetElevatorTargetHeightCommand moveToMinHeight,
            WristDownCommand gripperDeployDown,
            GripperIntakeCommand gripperIntake)
    {
        moveToMinHeight.setGoalHeight(elevator.getMinHeight());
        
        this.addParallel(moveToMinHeight);
        this.addParallel(gripperDeployDown);
        this.addParallel(gripperIntake);
    }
}
