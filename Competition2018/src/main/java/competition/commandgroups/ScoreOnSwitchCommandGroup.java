package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousPathSupplier;
import competition.subsystems.autonomous.AutonomousPathSupplier.SwitchScoringLocation;
import competition.subsystems.drive.commands.AbsolutePurePursuit2018Command;
import competition.subsystems.drive.commands.TotalRobotPoint;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import openrio.powerup.MatchData.GameFeature;
import xbot.common.command.BaseCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class ScoreOnSwitchCommandGroup extends BaseCommandGroup {

    public AbsolutePurePursuit2018Command pursuit;
    
    @Inject
    public ScoreOnSwitchCommandGroup(
            AutonomousPathSupplier decider,
            ElevatorSubsystem elevator,
            DelayViaSupplierCommand wait,
            AbsolutePurePursuit2018Command pursuit,
            SetWristAngleCommand setWristDown,
            SetElevatorTargetHeightCommand setElevatorForSwitch,
            // This is reversed, but I don't want to mess with the rest of the OI
            GripperEjectCommand eject) {
        this.pursuit = pursuit;
        pursuit.setPointSupplier(() -> decider.getPathToCorrectSwitch(SwitchScoringLocation.SideFacingAllianceWall));
        
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
