package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.DecendClimberCommand;
import competition.subsystems.climberdeploy.commands.ExtendClimberArmCommand;
import competition.subsystems.climberdeploy.commands.RetractClimberArmCommand;
import competition.subsystems.drive.commands.AssistedTankDriveCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.elevator.commands.CalibrateElevatorTicksPerInchCommand;
import competition.subsystems.elevator.commands.ElevatorMaintainerCommand;
import competition.subsystems.elevator.commands.LowerCommand;
import competition.subsystems.elevator.commands.RiseCommand;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.elevator.commands.CalibrateElevatorViaStallCommand;
import competition.subsystems.elevator.commands.CalibrateElevatorHereCommand;
import competition.subsystems.gripperdeploy.commands.GripperDeployDownCommand;
import competition.subsystems.gripperdeploy.commands.GripperDeployUpCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.shift.commands.ShiftHighCommand;
import competition.subsystems.shift.commands.ShiftLowCommand;
import competition.subsystems.shift.commands.ToggleGearCommand;
import competition.commandgroups.CollectCubeCommandGroup;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:
    /*
     * @Inject public void setupMyCommands( OperatorInterface operatorInterface, MyCommand myCommand) {
     * operatorInterface.leftButtons.getifAvailable(1).whenPressed(myCommand); }
     */

    @Inject
    public void setupDriveCommands(OperatorInterface oi, AssistedTankDriveCommand assistedTank,
            TankDriveWithJoysticksCommand simpleTank) {
        oi.driverGamepad.getifAvailable(9).whenPressed(assistedTank);
        oi.driverGamepad.getifAvailable(10).whenPressed(simpleTank);
    }

    @Inject
    public void setupShiftGearCommand(
            OperatorInterface oi, 
            ShiftHighCommand shiftHigh,
            ShiftLowCommand shiftLow) {
        
        oi.driverGamepad.getifAvailable(5).whenPressed(shiftLow);
        oi.driverGamepad.getifAvailable(6).whenPressed(shiftHigh);
    }

    @Inject
    public void setupGripperCommands(OperatorInterface oi, GripperDeployDownCommand down, GripperDeployUpCommand up,
            GripperEjectCommand eject, GripperIntakeCommand intake) {
        /*oi.operatorGamepad.getifAvailable(3).whileHeld(up);
        oi.operatorGamepad.getifAvailable(2).whileHeld(down);
        oi.operatorGamepad.getifAvailable(4).whenPressed(eject);
        oi.operatorGamepad.getifAvailable(1).whileHeld(intake);*/
    }

    @Inject
    public void setupElevatorCommands(
            OperatorInterface oi,
            LowerCommand lower,
            RiseCommand rise,
            CalibrateElevatorTicksPerInchCommand calibrateElevatorTicks,
            CalibrateElevatorViaStallCommand calibrate,
            ElevatorMaintainerCommand maintainer,
            SetElevatorTargetHeightCommand lowish,
            SetElevatorTargetHeightCommand highish,
            CalibrateElevatorHereCommand calibrateHere) {
        oi.operatorGamepad.getAnalogIfAvailable(oi.raiseElevator).whileActive(rise);
        oi.operatorGamepad.getAnalogIfAvailable(oi.lowerElevator).whileActive(lower);
        oi.operatorGamepad.getifAvailable(5).whileHeld(calibrateElevatorTicks);
        oi.operatorGamepad.getifAvailable(6).whenPressed(maintainer);
        oi.operatorGamepad.getifAvailable(7).whenPressed(calibrate);
        
        lowish.setGoalHeight(20);
        highish.setGoalHeight(60);
        
        oi.operatorGamepad.getifAvailable(1).whenPressed(lowish);
        oi.operatorGamepad.getifAvailable(2).whenPressed(highish);
        
        oi.operatorGamepad.getifAvailable(10).whenPressed(calibrateHere);
        
        
    }

    @Inject
    public void setupClimberCommands(OperatorInterface oi, AscendClimberCommand ascend, DecendClimberCommand decend,
            ExtendClimberArmCommand extendArm, RetractClimberArmCommand retractArm) {
        oi.driverGamepad.getifAvailable(1).whileHeld(extendArm);
        oi.driverGamepad.getifAvailable(2).whileHeld(retractArm);
        oi.driverGamepad.getAnalogIfAvailable(oi.raiseClimber).whileActive(ascend);
        oi.driverGamepad.getAnalogIfAvailable(oi.lowerClimber).whileActive(decend);
    }
    
    @Inject
    public void setupCollectCubeCommandGroup(OperatorInterface oi, CollectCubeCommandGroup collectCube) {
        oi.operatorGamepad.getifAvailable(9).whileHeld(collectCube);
    }
}
