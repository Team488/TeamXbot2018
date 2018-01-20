package competition.subsystems.climberdeploy.Command.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import competition.subsystems.climberdeploy.commands.DecreaseSpeedCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class DecreaseSpeedCommandTest extends BaseWPITest{
	
	ClimberdeploySubsystem deploy;
	DecreaseSpeedCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		deploy = injector.getInstance(ClimberdeploySubsystem.class);
		command = injector.getInstance(DecreaseSpeedCommand.class);
		deploy.temporaryHack();
	}
	
	@Test
	public void simpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkDecreaseSpeed() {
		deploy.extendClimberArm();
		assertEquals(0.2, deploy.motor.getMotorOutputPercent(), 0.001);
		command.initialize();
		command.execute();
		deploy.extendClimberArm();
		assertEquals(0.1, deploy.motor.getMotorOutputPercent(), 0.001);
	}
}