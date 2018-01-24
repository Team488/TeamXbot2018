package competition.subsystems.gripperdeploy.Commands.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import competition.subsystems.gripperdeploy.commands.DeployUpCommand;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class DeployUpCommandTest extends BaseWPITest{
	
	GripperDeploySubsystem gripperDeploy;
	DeployUpCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		gripperDeploy = injector.getInstance(GripperDeploySubsystem.class);
		command = injector.getInstance(DeployUpCommand.class);
		
		gripperDeploy.temporaryHack();
	}
	
	@Test
	public void SimpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkDeployUp() {
		assertEquals(0.0, gripperDeploy.motor.getMotorOutputPercent(), 0.001);
		command.initialize();
		command.execute();
		assertEquals(0.5, gripperDeploy.motor.getMotorOutputPercent(), 0.001);
	}
}