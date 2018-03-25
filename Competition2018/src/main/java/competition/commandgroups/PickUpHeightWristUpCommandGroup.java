package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import xbot.common.command.BaseCommandGroup;

public class PickUpHeightWristUpCommandGroup extends BaseCommandGroup {
    
    @Inject
    public PickUpHeightWristUpCommandGroup(
            SetWristAngleCommand wristUp, 
            SetElevatorTargetHeightCommand targetPickUpHeight) {
        this.addSequential(wristUp);
        this.addSequential(targetPickUpHeight);
    }
}
