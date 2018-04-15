package competition.commandgroups;

import com.google.inject.Inject;
import com.google.inject.Provider;

import competition.subsystems.autonomous.AutonomousPathSupplier;
import competition.subsystems.autonomous.AutonomousPathSupplier.SwitchScoringLocation;
import competition.subsystems.drive.commands.AbsolutePurePursuit2018Command;
import competition.subsystems.drive.commands.TotalRobotPoint;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.gripperintake.commands.GripperStopCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import openrio.powerup.MatchData.GameFeature;
import xbot.common.command.BaseCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.command.RunCommandAfterDelayCommand;
import xbot.common.command.TimeoutCommand;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

public class MultiCubeScoreOnSwitchCommandGroup extends BaseCommandGroup {

    public AbsolutePurePursuit2018Command pursuit;
    
    @Inject
    public MultiCubeScoreOnSwitchCommandGroup(
            AutonomousPathSupplier decider,
            ElevatorSubsystem elevator,
            DelayViaSupplierCommand wait,
            AbsolutePurePursuit2018Command pursuit,
            AbsolutePurePursuit2018Command getCube,
            AbsolutePurePursuit2018Command returnToSwitch,
            SetWristAngleCommand setWristDown,
            SetElevatorTargetHeightCommand setElevatorForSwitch,
            SetElevatorTargetHeightCommand setElevatorToPickUpCube,
            GripperStopCommand stop,
            GripperIntakeCommand intake,
            Provider<TimeoutCommand> timeoutProvider,
            // This is reversed, but I don't want to mess with the rest of the OI
            GripperEjectCommand eject) {
        this.pursuit = pursuit;
        pursuit.setPointSupplier(() -> decider.getPathToCorrectSwitch(SwitchScoringLocation.SideFacingAllianceWall));
        getCube.setPointSupplier(() -> decider.getAdvancedPathToNearbyCubeFromSwitchPlate());
        returnToSwitch.setPointSupplier(() -> decider.getAdvancedPathToNearbySwitchFromSecondCube());
        
        setWristDown.setGoalAngle(0);
        setElevatorForSwitch.setGoalHeight(elevator.getTargetSwitchDropHeight());
        setElevatorToPickUpCube.setGoalHeight(elevator.getTargetPickUpHeight());
        wait.setDelaySupplier(() -> decider.getDelay());
        
        this.addSequential(wait);
        // Get ready to score
        this.addParallel(setWristDown, 1);
        this.addParallel(setElevatorForSwitch, 1);
        this.addSequential(pursuit);
        
        // Score for 1 second
        this.addSequential(eject, 1);
        
        this.addParallel(new RunCommandAfterDelayCommand(setElevatorToPickUpCube, .75, timeoutProvider));
        
        this.addParallel(intake, 1);
        
        this.addSequential(getCube);
        this.addParallel(new RunCommandAfterDelayCommand(stop, .1, timeoutProvider));
        
        this.addSequential(returnToSwitch);
    }
}
