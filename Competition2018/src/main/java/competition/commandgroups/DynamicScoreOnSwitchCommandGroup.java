package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import openrio.powerup.MatchData.GameFeature;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.PurePursuitCommand;

public class DynamicScoreOnSwitchCommandGroup extends BaseCommandGroup {

    public PurePursuitCommand pursuit;
    
    @Inject
    public DynamicScoreOnSwitchCommandGroup(
            AutonomousDecisionSystem decider,
            ElevatorSubsystem elevator,
            PurePursuitCommand pursuit,
            SetWristAngleCommand setWristDown,
            SetElevatorTargetHeightCommand setElevatorForSwitch,
            GripperEjectCommand eject) {
        this.pursuit = pursuit;
        pursuit.setPointSupplier(decider.getAutoPathToFeature(GameFeature.SWITCH_NEAR, false));
        
        //setWristDown.setGoalAngle(0);
        //setElevatorForSwitch.setGoalHeight(elevator.getTargetSwitchDropHeight());
        // TODO: Uncomment these once the elevator/wrist is trustworthy.
        // Get ready to score
        //this.addParallel(setWristDown, 1);
        //this.addParallel(setElevatorForSwitch, 1);
        this.addSequential(pursuit);
        
        // Score for 1 second
        //this.addSequential(eject, 1);
    }
}
