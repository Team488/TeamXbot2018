package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousPathSupplier;
import competition.subsystems.drive.commands.AbsolutePurePursuit2018Command;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.MoveElevatorToHeightAndStabilizeCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.gripperintake.commands.GripperStopCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;

@Deprecated
public class MultiCubeNearScaleCommandGroup extends BaseCommandGroup {

    public AbsolutePurePursuit2018Command pursuit;
    
    @Inject
    public MultiCubeNearScaleCommandGroup(
            AutonomousPathSupplier pathSupplier,
            ElevatorSubsystem elevator,
            DelayViaSupplierCommand wait,
            AbsolutePurePursuit2018Command pursuit,
            SetWristAngleCommand setWristDown,
            SetWristAngleCommand setWristDownAgain,
            ConfigurablePurePursuitCommand scootForward,
            MoveElevatorToHeightAndStabilizeCommand setElevatorForScale,
            MoveElevatorToHeightAndStabilizeCommand setElevatorForScaleAgain,
            MoveElevatorToHeightAndStabilizeCommand lowerElevator,
            SetWristAngleCommand setWristUp,
            SetWristAngleCommand setWristUpAgain,
            AbsolutePurePursuit2018Command getCube,
            AbsolutePurePursuit2018Command returnToScale,
            GripperIntakeCommand intake,
            GripperEjectCommand eject,
            GripperEjectCommand ejectAgain,
            GripperStopCommand stopCollector
            ) {
        /*this.pursuit = pursuit;
        
        pursuit.setPointSupplier(() -> pathSupplier.getAdvancedPathToNearbyScalePlate());
        getCube.setPointSupplier(() -> pathSupplier.getAdvancedPathToNearbyCubeFromScalePlate());
        returnToScale.setPointSupplier(() -> pathSupplier.getAdvancedPathBackToScalePlateFromCube());

        setWristDown.setGoalAngle(45);
        setWristDownAgain.setGoalAngle(0);
        setWristUp.setGoalAngle(45);
        setWristUpAgain.setGoalAngle(45);
        
        setElevatorForScale.setTargetHeight(elevator.getTargetScaleMidHeight());
        setElevatorForScaleAgain.setTargetHeight(elevator.getTargetScaleMidHeight());
        lowerElevator.setTargetHeight(elevator.getTargetPickUpHeight());
        
        wait.setDelaySupplier(() -> pathSupplier.getDelay());
        
        
        this.addSequential(wait);
        
        // Get in position
        this.addSequential(pursuit);
        
        // Now we've stopped, so put the wrist down and the elevator up
        this.addParallel(setWristDown, 1);
        this.addSequential(setElevatorForScale);
        
        // Score for 1 second
        this.addSequential(eject, 1);
                
        //this.addSequential(setWristUp, 0.33);
        this.addParallel(lowerElevator);
        
        // intake later
        this.addParallel(setWristDownAgain, 1);
        this.addParallel(intake, 1);
        this.addSequential(getCube);
        
        this.addParallel(stopCollector);
        this.addSequential(returnToScale);

        this.addParallel(setWristUpAgain, 1);
        this.addSequential(setElevatorForScaleAgain);
                
        // eject
        this.addSequential(ejectAgain, 1);
        */
    }
}
