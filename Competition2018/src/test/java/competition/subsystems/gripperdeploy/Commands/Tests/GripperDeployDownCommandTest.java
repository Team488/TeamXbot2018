package competition.subsystems.gripperdeploy.Commands.Tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import competition.subsystems.gripperdeploy.commands.GripperDeployDownCommand;
import xbot.common.injection.BaseWPITest;

public class GripperDeployDownCommandTest extends BaseWPITest{
	
	GripperDeploySubsystem gripperDeploy;
	GripperDeployDownCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		gripperDeploy = injector.getInstance(GripperDeploySubsystem.class);
		command = injector.getInstance(GripperDeployDownCommand.class);
		
		gripperDeploy.temporaryHack();
	}
	
	@Test
	public void SimpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkDeployDown() {
		assertEquals(0.0, gripperDeploy.motor.getMotorOutputPercent(), 0.001);
		command.initialize();
		command.execute();
		assertEquals(-0.5, gripperDeploy.motor.getMotorOutputPercent(), 0.001);
	}
}