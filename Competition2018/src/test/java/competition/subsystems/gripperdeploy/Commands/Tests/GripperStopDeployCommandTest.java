package competition.subsystems.gripperdeploy.Commands.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import competition.subsystems.gripperdeploy.commands.DeployUpCommand;
import competition.subsystems.gripperdeploy.commands.StopDeployCommand;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;
import xbot.common.injection.wpi_factories.CommonLibFactory;

public class StopDeployCommandTest extends BaseWPITest{

	GripperDeploySubsystem gripperDeploy;
	StopDeployCommand command;
	
	@Override
	public void setUp() {
		super.setUp();
		
		gripperDeploy = injector.getInstance(GripperDeploySubsystem.class);
		command = injector.getInstance(StopDeployCommand.class);
		
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
