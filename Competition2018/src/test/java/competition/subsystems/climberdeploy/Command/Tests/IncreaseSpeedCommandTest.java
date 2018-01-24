package competition.subsystems.climberdeploy.Command.Tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.climberdeploy.commands.IncreaseSpeedCommand;
import xbot.common.injection.BaseWPITest;

public class IncreaseSpeedCommandTest extends BaseWPITest{
	
	ClimberDeploySubsystem deploy;
	IncreaseSpeedCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		deploy = injector.getInstance(ClimberDeploySubsystem.class);
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
		deploy.decreaseSpeed();
		deploy.extendClimberArm();
		assertEquals(0.1, deploy.motor.getMotorOutputPercent(), 0.001);
		command.initialize();
		command.execute();
		deploy.extendClimberArm();
		assertEquals(0.4, deploy.motor.getMotorOutputPercent(), 0.001);
	}
}