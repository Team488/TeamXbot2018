package competition.subsystems.climberdeploy.Command.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import competition.subsystems.climberdeploy.commands.StopClimberArmCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class StopClimberArmCommandTest extends BaseWPITest{
	
	ClimberdeploySubsystem deploy;
	StopClimberArmCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		deploy = injector.getInstance(ClimberdeploySubsystem.class);
		command = injector.getInstance(StopClimberArmCommand.class);
		
		deploy.temporaryHack();
	}
	
	@Test
	public void simpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkStopClimberArm() {
		command.initialize();
		command.execute();
		
		assertEquals(0.0, deploy.motor.getMotorOutputPercent(), 0.001);
	}
}