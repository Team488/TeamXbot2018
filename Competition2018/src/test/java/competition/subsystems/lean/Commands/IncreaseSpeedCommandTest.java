package competition.subsystems.lean.Commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.IncreaseSpeedCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class IncreaseSpeedCommandTest extends BaseWPITest{
	
	LeanSubsystem lean;
	IncreaseSpeedCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		lean = injector.getInstance(LeanSubsystem.class);
		command = injector.getInstance(IncreaseSpeedCommand.class);
		
		lean.temporaryHack();
	}
	
	@Test
	public void simpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkIncreaseSpeed() {
		assertEquals(0.0, lean.motor.getMotorOutputPercent(), 0.001);
		command.initialize();
		command.execute();
		assertEquals(.4, lean.motor.getMotorOutputPercent(), 0.001);
	}
}
