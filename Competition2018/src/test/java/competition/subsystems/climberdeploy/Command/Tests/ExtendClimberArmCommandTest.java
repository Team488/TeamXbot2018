package competition.subsystems.climberdeploy.Command.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import competition.subsystems.climberdeploy.commands.ExtendClimberArmCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class ExtendClimberArmCommandTest extends BaseWPITest{
	
	ClimberdeploySubsystem deploy;
	ExtendClimberArmCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		deploy = injector.getInstance(ClimberdeploySubsystem.class);
		command = injector.getInstance(ExtendClimberArmCommand.class);
		deploy.temporaryHack();
	}
	
	@Test
	public void simpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkExtendClimberArm() {
		command.initialize();
		assertEquals(0.0, deploy.motor.getMotorOutputPercent(), 0.001);
		command.execute();
		assertEquals(0.2, deploy.motor.getMotorOutputPercent(), 0.001);
	}
}
