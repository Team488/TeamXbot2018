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
import xbot.common.subsystems.drive.PurePursuitCommand;

public class DynamicScoreOnScaleCommandGroup extends BaseCommandGroup {

    public PurePursuitCommand pursuit;
    
    @Inject
    public DynamicScoreOnScaleCommandGroup(
            AutonomousDecisionSystem decider,
            ElevatorSubsystem elevator,
            DelayViaSupplierCommand wait,
            PurePursuitCommand pursuit,
            SetWristAngleCommand setWristDown,
            SetElevatorTargetHeightCommand setElevatorForScale,
            GripperEjectCommand eject) {
        this.pursuit = pursuit;
        pursuit.setPointSupplier(decider.getAutoPathToFeature(GameFeature.SCALE));
        
        setWristDown.setGoalAngle(0);
        setElevatorForScale.setGoalHeight(elevator.getTargetScaleHighHeight());
        wait.setDelaySupplier(() -> decider.getDelay());
        
        this.addSequential(wait);
        // TODO: Uncomment these once the elevator/wrist is trustworthy.
        // Get ready to score
        this.addParallel(setWristDown, 1);
        this.addParallel(setElevatorForScale, 1);
        this.addSequential(pursuit);
        
        // Score for 1 second
        this.addSequential(eject, 1);
    }
}
