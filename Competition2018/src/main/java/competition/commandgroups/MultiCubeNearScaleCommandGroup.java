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
            AbsolutePurePursuit2018Command returnToScaleA,
            AbsolutePurePursuit2018Command returnToScaleB,
            GripperIntakeCommand intake,
            GripperEjectCommand eject,
            GripperEjectCommand ejectAgain,
            GripperStopCommand stopCollector
            ) {
        this.pursuit = pursuit;
        
        pursuit.setPointSupplier(() -> pathSupplier.getPathToAlignedScaleFast());
        getCube.setPointSupplier(() -> pathSupplier.getAdvancedPathToNearbyCubeFromScalePlate());
        returnToScaleA.setPointSupplier(() -> pathSupplier.getAdvancedPathToNearbyScalePlateFromSecondCubeA());
        returnToScaleB.setPointSupplier(() -> pathSupplier.getAdvancedPathToNearbyScalePlateFromSecondCubeB());
        
        setWristDown.setGoalAngle(45);
        setWristDownAgain.setGoalAngle(0);
        setWristUp.setGoalAngle(90);
        setWristUpAgain.setGoalAngle(45);
        
        setElevatorForScale.setTargetHeight(elevator.getTargetScaleMidHeight());
        setElevatorForScaleAgain.setTargetHeight(elevator.getTargetScaleMidHeight());
        lowerElevator.setTargetHeight(elevator.getTargetPickUpHeight());
        
        wait.setDelaySupplier(() -> pathSupplier.getDelay());
        
        
        this.addSequential(wait);
        
        // Now we've stopped, so put the wrist down and the elevator up
        this.addParallel(setWristDown, 1);
        this.addParallel(setElevatorForScale);
        
        // Get in position
        this.addSequential(pursuit);
        
        // Score for 1 second
        this.addSequential(eject, 0.5);
        
        //this.addSequential(setWristUp);
        this.addParallel(lowerElevator);
        
        // intake later
        this.addParallel(setWristDownAgain, 0.1);
        this.addParallel(intake, 1);
        this.addSequential(getCube);
        
        // parallel
        this.addSequential(stopCollector, 0.1);
        
        this.addSequential(returnToScaleA);

        this.addParallel(setWristUpAgain, 0.1);
        this.addParallel(setElevatorForScaleAgain);
        
        this.addSequential(returnToScaleB);
                
        // eject
        this.addSequential(ejectAgain, 1);
    }
}
