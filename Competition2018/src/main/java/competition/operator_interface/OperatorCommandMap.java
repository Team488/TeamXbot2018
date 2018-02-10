package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.offboard.commands.AcquireVisibleCubeCommand;
import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.DecendClimberCommand;
import competition.subsystems.climberdeploy.commands.ExtendClimberArmCommand;
import competition.subsystems.climberdeploy.commands.RetractClimberArmCommand;
import competition.subsystems.drive.commands.AssistedTankDriveCommand;
import competition.subsystems.drive.commands.DriveAtVelocityCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.elevator.commands.CalibrateElevatorTicksPerInchCommand;
import competition.subsystems.elevator.commands.LowerCommand;
import competition.subsystems.elevator.commands.RiseCommand;
import competition.subsystems.elevator.commands.CalibrateElevatorCommand;
import competition.subsystems.gripperdeploy.commands.GripperDeployDownCommand;
import competition.subsystems.gripperdeploy.commands.GripperDeployUpCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.shift.commands.ToggleGearCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
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
            TankDriveWithJoysticksCommand simpleTank, DriveAtVelocityCommand driveAtVelocity) {
        oi.driverGamepad.getifAvailable(9).whenPressed(assistedTank);
        oi.driverGamepad.getifAvailable(10).whenPressed(simpleTank);
        oi.driverGamepad.getifAvailable(1).whileHeld(driveAtVelocity);
    }

    @Inject
    public void setupShiftGearCommand(OperatorInterface oi, ToggleGearCommand shiftGear) {
        oi.driverGamepad.getifAvailable(6).whenPressed(shiftGear);
    }

    @Inject
    public void setupGripperCommands(OperatorInterface oi, GripperDeployDownCommand down, GripperDeployUpCommand up,
            GripperEjectCommand eject, GripperIntakeCommand intake) {
        //oi.operatorGamepad.getifAvailable(3).whileHeld(up);
        //oi.operatorGamepad.getifAvailable(2).whileHeld(down);
        //oi.operatorGamepad.getifAvailable(4).whenPressed(eject);
        //oi.operatorGamepad.getifAvailable(1).whileHeld(intake);
    }

    @Inject
    public void setupElevatorCommands(
            OperatorInterface oi,
            LowerCommand lower,
            RiseCommand rise,
            CalibrateElevatorTicksPerInchCommand calibrateElevatorTicks,
            CalibrateElevatorCommand calibrate) {
        /*oi.operatorGamepad.getAnalogIfAvailable(oi.raiseElevator).whileActive(rise);
        oi.operatorGamepad.getAnalogIfAvailable(oi.lowerElevator).whileActive(lower);
        oi.operatorGamepad.getifAvailable(5).whileHeld(calibrateElevatorTicks);
        oi.operatorGamepad.getifAvailable(7).whenPressed(calibrate);*/
    }

    @Inject
    public void setupClimberCommands(OperatorInterface oi, AscendClimberCommand ascend, DecendClimberCommand decend,
            ExtendClimberArmCommand extendArm, RetractClimberArmCommand retractArm) {
        //oi.driverGamepad.getifAvailable(1).whileHeld(extendArm);
        //oi.driverGamepad.getifAvailable(2).whileHeld(retractArm);
        //oi.driverGamepad.getAnalogIfAvailable(oi.raiseClimber).whileActive(ascend);
        //oi.driverGamepad.getAnalogIfAvailable(oi.lowerClimber).whileActive(decend);
    }
    
    @Inject
    public void setupCollectCubeCommandGroup(OperatorInterface oi, CollectCubeCommandGroup collectCube) {
        //oi.operatorGamepad.getifAvailable(6).whileHeld(collectCube);
    }
    
    @Inject
    public void setupVisionCommands(OperatorInterface oi, AcquireVisibleCubeCommand acquireCube) {
        oi.driverGamepad.getifAvailable(3).whilePressedNoRestart(acquireCube);
    }
}
