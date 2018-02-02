package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.shift.commands.*;
import competition.subsystems.gripperdeploy.commands.*;
import competition.subsystems.gripperintake.commands.*;
import competition.subsystems.elevator.commands.*;
import competition.subsystems.climb.commands.*;
import competition.subsystems.climberdeploy.commands.*;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:
    /*
     * @Inject public void setupMyCommands( OperatorInterface operatorInterface, MyCommand myCommand) {
     * operatorInterface.leftButtons.getifAvailable(1).whenPressed(myCommand); }
     */

    @Inject
    public void setupShiftGearCommand(OperatorInterface oi, ToggleGearCommand shiftGear) {
        oi.driverGamepad.getifAvailable(6).whenPressed(shiftGear);
    }

    @Inject
    public void setupGripperCommands(OperatorInterface oi, GripperDeployDownCommand down, GripperDeployUpCommand up,
            GripperEjectCommand eject, GripperIntakeCommand intake) {
        oi.operatorGamepad.getifAvailable(3).whenPressed(up);
        oi.operatorGamepad.getifAvailable(2).whenPressed(down);
        oi.operatorGamepad.getifAvailable(4).whenPressed(eject);
        oi.operatorGamepad.getifAvailable(1).whenPressed(intake);
    }

    @Inject
    public void setupElevatorCommands(OperatorInterface oi, LowerCommand lower, RiseCommand rise) {
        oi.operatorGamepad.getAnalogIfAvailable(oi.raiseElevator).whileActive(rise);
        oi.operatorGamepad.getAnalogIfAvailable(oi.lowerElevator).whileActive(lower);
    }

    @Inject
    public void setupClimberCommands(OperatorInterface oi, AscendClimberCommand ascend, DecendClimberCommand decend,
            ExtendClimberArmCommand extendArm, RetractClimberArmCommand retractArm) {
        oi.driverGamepad.getifAvailable(1).whenPressed(extendArm);
        oi.driverGamepad.getifAvailable(2).whenPressed(retractArm);
        oi.driverGamepad.getAnalogIfAvailable(oi.raiseClimber).whileActive(ascend);
        oi.driverGamepad.getAnalogIfAvailable(oi.lowerClimber).whileActive(decend);
    }
}
