package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.offboard.commands.AcquireVisibleCubeCommand;
import competition.subsystems.shift.commands.ToggleGearCommand;

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
    public void setupVisionCommands(OperatorInterface oi, AcquireVisibleCubeCommand acquireCube) {
        oi.gamepad.getifAvailable(1).whilePressedNoRestart(acquireCube);
    }
    
	@Inject
	public void setupShiftGearCommand(
			OperatorInterface oi,
			ToggleGearCommand shiftGear
			) {
		oi.gamepad.getifAvailable(4).whenPressed(shiftGear);
	}
}
