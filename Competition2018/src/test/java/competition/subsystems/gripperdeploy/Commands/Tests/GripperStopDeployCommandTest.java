package competition.subsystems.gripperdeploy.Commands.Tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import competition.subsystems.gripperdeploy.commands.GripperStopDeployCommand;
import xbot.common.injection.BaseWPITest;

public class GripperStopDeployCommandTest extends BaseWPITest{

	GripperDeploySubsystem gripperDeploy;
	GripperStopDeployCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		gripperDeploy = injector.getInstance(GripperDeploySubsystem.class);
		command = injector.getInstance(GripperStopDeployCommand.class);
		
		gripperDeploy.temporaryHack();
	}
	
	@Test
	public void SimpleTest() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void checkStopDeploy() {
		gripperDeploy.deployUp();
		assertEquals(0.5, gripperDeploy.motor.getMotorOutputPercent(), 0.001);
		command.initialize();
		command.execute();
		assertEquals(0.0, gripperDeploy.motor.getMotorOutputPercent(), 0.001);
	}
}
