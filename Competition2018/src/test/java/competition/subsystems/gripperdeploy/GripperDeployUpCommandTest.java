package competition.subsystems.gripperdeploy;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import competition.subsystems.gripperdeploy.commands.GripperDeployUpCommand;
import xbot.common.injection.BaseWPITest;

public class GripperDeployUpCommandTest extends BaseWPITest{
	
	GripperDeploySubsystem gripperDeploy;
	GripperDeployUpCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		gripperDeploy = injector.getInstance(GripperDeploySubsystem.class);
		command = injector.getInstance(GripperDeployUpCommand.class);
		
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