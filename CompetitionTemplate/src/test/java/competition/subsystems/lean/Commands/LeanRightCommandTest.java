package competition.subsystems.lean.Commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.LeanRightCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class LeanRightCommandTest extends BaseWPITest{
	
	LeanSubsystem lean;
	LeanRightCommand command;
	
	@Override
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		
		lean = injector.getInstance(LeanSubsystem.class);
		command = injector.getInstance(LeanRightCommand.class);
		
		lean.temporaryHack();
	}
	
	@Test
	public void simpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkLeanRight() {
		command.initialize();
		command.execute();
		
		assertEquals(-0.2, lean.motor.getMotorOutputPercent(), 0.001);
	}
}