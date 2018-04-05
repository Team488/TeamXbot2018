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

public class MultiCubeNearSwitchCommandGroup extends BaseCommandGroup {
    
    public AbsolutePurePursuit2018Command pursuit;
    
    @Inject
    public MultiCubeNearSwitchCommandGroup(
        AutonomousPathSupplier pathSupplier,
        ElevatorSubsystem elevator,
        DelayViaSupplierCommand wait,
        AbsolutePurePursuit2018Command pursuit,
        SetWristAngleCommand setWristDown,
        SetWristAngleCommand setWristDownAgain,
        ConfigurablePurePursuitCommand scootForward,
        MoveElevatorToHeightAndStabilizeCommand setElevatorForSwitch,
        MoveElevatorToHeightAndStabilizeCommand setElevatorForSwitchAgain,
        MoveElevatorToHeightAndStabilizeCommand lowerElevator,
        SetWristAngleCommand setWristUp,
        SetWristAngleCommand setWristUpAgain,
        AbsolutePurePursuit2018Command getCube,
        AbsolutePurePursuit2018Command returnToSwitch,
        GripperIntakeCommand intake,
        GripperEjectCommand eject,
        GripperEjectCommand ejectAgain,
        GripperStopCommand stopCollector
        ) {
        this.pursuit = pursuit;
        
        pursuit.setPointSupplier(() -> pathSupplier.getAdvancedPathToNearbySwitchPlateFromMiddle());
        getCube.setPointSupplier(() -> pathSupplier.getAdvancedPathToNearbyCubeFromSwitchPlate());
        returnToSwitch.setPointSupplier(() -> pathSupplier.getAdvancedPathBackToSwitchPlateFromCube());
        
        setWristDown.setGoalAngle(45);
        setWristDownAgain.setGoalAngle(0);
        setWristUp.setGoalAngle(45);
        setWristUpAgain.setGoalAngle(45);
        
        setElevatorForSwitch.setTargetHeight(elevator.getTargetSwitchDropHeight());
        setElevatorForSwitchAgain.setTargetHeight(elevator.getTargetSwitchDropHeight());
        lowerElevator.setTargetHeight(elevator.getTargetPickUpHeight());
        
        wait.setDelaySupplier(() -> pathSupplier.getDelay());
        
        this.addSequential(wait);

        // Go to switch
        this.addSequential(pursuit);
        
        // Now we've stopped, put wrist down and elevator go up
        this.addParallel(setWristDown, 1);
        this.addSequential(setElevatorForSwitch);
        
        // Score cube to switch
        this.addSequential(eject, 1);
        this.addSequential(lowerElevator);
        
        // Intake later
        this.addParallel(setWristDownAgain, 1);
        this.addParallel(intake, 1);
        this.addSequential(getCube);
        
        this.addParallel(stopCollector);
        this.addSequential(returnToSwitch);
        
        this.addParallel(setWristUpAgain, 1);
        this.addSequential(setElevatorForSwitchAgain);
        
        // Eject
        this.addSequential(ejectAgain, 1);
    }

}
