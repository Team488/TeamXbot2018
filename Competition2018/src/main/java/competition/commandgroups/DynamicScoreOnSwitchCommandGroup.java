package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import openrio.powerup.MatchData.GameFeature;
import xbot.common.command.BaseCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class DynamicScoreOnSwitchCommandGroup extends BaseCommandGroup {

    public ConfigurablePurePursuitCommand pursuit;
    
    @Inject
    public DynamicScoreOnSwitchCommandGroup(
            AutonomousDecisionSystem decider,
            ElevatorSubsystem elevator,
            DelayViaSupplierCommand wait,
            ConfigurablePurePursuitCommand pursuit,
            SetWristAngleCommand setWristDown,
            SetElevatorTargetHeightCommand setElevatorForSwitch,
            // This is reversed, but I don't want to mess with the rest of the OI
            GripperEjectCommand eject) {
        this.pursuit = pursuit;
        pursuit.setPointSupplier(decider.getAutoPathToFeature(GameFeature.SWITCH_NEAR));
        
        setWristDown.setGoalAngle(0);
        setElevatorForSwitch.setGoalHeight(elevator.getTargetSwitchDropHeight());
        wait.setDelaySupplier(() -> decider.getDelay());
        
        this.addSequential(wait);
        // Get ready to score
        this.addParallel(setWristDown, 1);
        this.addParallel(setElevatorForSwitch, 1);
        this.addSequential(pursuit);
        
        // Score for 1 second
        this.addSequential(eject, 1);
    }
}
