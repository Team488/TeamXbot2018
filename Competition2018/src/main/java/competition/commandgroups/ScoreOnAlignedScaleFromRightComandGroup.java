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
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.PurePursuitCommand.PursuitMode;

public class ScoreOnAlignedScaleFromRightComandGroup extends BaseCommandGroup {

    public ConfigurablePurePursuitCommand pursuit;

    @Inject
    public ScoreOnAlignedScaleFromRightComandGroup(AutonomousDecisionSystem decider, ElevatorSubsystem elevator,
            DelayViaSupplierCommand wait, ConfigurablePurePursuitCommand pursuit, SetWristAngleCommand setWristDown,
            SetElevatorTargetHeightCommand setElevatorForScale, ConfigurablePurePursuitCommand scootForward,
            GripperEjectCommand eject, ConfigurablePurePursuitCommand scootBackward,
            ConfigurablePurePursuitCommand firstCubeFromGround, SetWristAngleCommand setWristToPickUp,
            SetElevatorTargetHeightCommand setElevatorForPickup) {
        this.pursuit = pursuit;
        pursuit.setPointSupplier(decider.getAutoPathToFeature(GameFeature.SCALE));
        scootForward.addPoint(new FieldPose(new XYPair(0, 2.666 * 12), new ContiguousHeading(90)));
        scootForward.setMode(PursuitMode.Relative);
        scootBackward.addPoint(new FieldPose(new XYPair(0, -2.666 * 12), new ContiguousHeading(90)));
        scootBackward.setMode(PursuitMode.Relative);
        firstCubeFromGround.addPoint(new FieldPose(new XYPair(-2 * 12, 15 * 12), new ContiguousHeading()));

        setWristDown.setGoalAngle(45);
        setWristToPickUp.setGoalAngle(0);
        setElevatorForScale.setGoalHeight(elevator.getTargetScaleMidHeight());
        setElevatorForPickup.setGoalHeight(elevator.getMinHeight());
        wait.setDelaySupplier(() -> decider.getDelay());

        this.addSequential(wait);

        // Get in position
        this.addSequential(pursuit);

        // Now we've stopped, so put the wrist down and the elevator up
        this.addParallel(setWristDown, 1);
        setElevatorForScale.changeTimeout(3.5);
        this.addSequential(setElevatorForScale);

        // scoot forward a little
        this.addSequential(scootForward);

        // Score for 1 second
        this.addSequential(eject, 1);

        // scoot backward a little
        this.addSequential(scootBackward);

        // go to first cube on ground position
        this.addSequential(firstCubeFromGround);

        // brings wrist to pickup position and elevator down
        this.addParallel(setWristToPickUp, 1);
        setElevatorForPickup.changeTimeout(3.5);
        this.addSequential(setElevatorForPickup);

        // Get in position
        this.addSequential(pursuit);

        // Now we've stopped, so put the wrist down and the elevator up
        this.addParallel(setWristDown, 1);
        setElevatorForScale.changeTimeout(3.5);
        this.addSequential(setElevatorForScale);
    }
}
