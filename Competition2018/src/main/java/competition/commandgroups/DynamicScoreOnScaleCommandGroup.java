package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousPathSupplier;
import competition.subsystems.drive.commands.AbsolutePurePursuit2018Command;
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
import xbot.common.subsystems.drive.PurePursuitCommand.PointLoadingMode;
import xbot.common.subsystems.drive.RabbitPoint;

public class DynamicScoreOnScaleCommandGroup extends BaseCommandGroup {

    public AbsolutePurePursuit2018Command pursuit;
    
    @Inject
    public DynamicScoreOnScaleCommandGroup(
            AutonomousPathSupplier decider,
            ElevatorSubsystem elevator,
            DelayViaSupplierCommand wait,
            AbsolutePurePursuit2018Command pursuit,
            SetWristAngleCommand setWristDown,
            SetElevatorTargetHeightCommand setElevatorForScale,
            GripperEjectCommand eject) {
        this.pursuit = pursuit;
        pursuit.setPointSupplier(decider.getAdvancedAutoPathToScale());
        
        setWristDown.setGoalAngle(60);
        setElevatorForScale.setGoalHeight(elevator.getTargetScaleMidHeight());
        wait.setDelaySupplier(() -> decider.getDelay());
        
        this.addSequential(wait);
        
        // Get in position
        this.addSequential(pursuit);
        
        // Now we've stopped, so put the wrist down and the elevator up
        setElevatorForScale.changeTimeout(2);
        this.addSequential(setElevatorForScale);
        this.addSequential(setWristDown, 1);
        
        // Score for 1 second
        this.addSequential(eject, 1);
    }
}
