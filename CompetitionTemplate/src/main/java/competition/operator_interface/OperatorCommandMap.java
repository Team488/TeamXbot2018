package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.shift.commands.ShiftGearCommand;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:
    /*
    @Inject
    public void setupMyCommands(
            OperatorInterface operatorInterface,
            MyCommand myCommand)
    {
        operatorInterface.leftButtons.getifAvailable(1).whenPressed(myCommand);
    }
    */
	
	@Inject
	public void setupShiftGearCommand(
			OperatorInterface oi,
			ShiftGearCommand shiftGear
			) {
		oi.leftJoystick.getifAvailable(8).whenPressed(shiftGear);
	}
}
