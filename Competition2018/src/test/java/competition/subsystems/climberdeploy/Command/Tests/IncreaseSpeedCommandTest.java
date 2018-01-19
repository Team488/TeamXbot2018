package competition.subsystems.climberdeploy.Command.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import competition.subsystems.climberdeploy.commands.IncreaseSpeedCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class IncreaseSpeedCommandTest extends BaseWPITest{
	
	ClimberdeploySubsystem deploy;
	IncreaseSpeedCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		deploy = injector.getInstance(ClimberdeploySubsystem.class);
		command = injector.getInstance(IncreaseSpeedCommand.class);
		
		deploy.temporaryHack();
	}
	
	@Test
	public void simpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkIncreaseSpeed() {
		command.initialize();
		command.execute();
		
		assertEquals(0.4, deploy.motor.getMotorOutputPercent(), 0.001);
	}
}