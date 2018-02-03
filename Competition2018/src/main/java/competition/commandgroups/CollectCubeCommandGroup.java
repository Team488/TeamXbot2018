package competition.commandgroups;

import com.google.inject.Inject;

import edu.wpi.first.wpilibj.command.CommandGroup;
import competition.subsystems.elevator.commands.MoveToMinHeightCommand;
import competition.subsystems.gripperdeploy.commands.GripperDeployDownCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;

public class CollectCubeCommandGroup extends CommandGroup{
    
    @Inject
    public CollectCubeCommandGroup(MoveToMinHeightCommand moveToMinHeight,
            GripperDeployDownCommand gripperDeployDown,
            GripperIntakeCommand gripperIntake)
    {
        this.addParallel(moveToMinHeight);
        this.addParallel(gripperDeployDown);
        this.addParallel(gripperIntake);
    }
}
