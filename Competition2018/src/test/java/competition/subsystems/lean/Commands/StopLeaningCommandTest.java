package competition.subsystems.lean.Commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.StopLeaningCommand;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;
import xbot.common.injection.wpi_factories.CommonLibFactory;

public class StopLeaningCommandTest extends BaseWPITest{
	
	LeanSubsystem lean;
	StopLeaningCommand command;

	
	@Override
	public void setUp() {
		super.setUp();
		
		lean = injector.getInstance(LeanSubsystem.class);
		command = injector.getInstance(StopLeaningCommand.class);
		
		lean.temporaryHack();
	}
	
	@Test
	public void simpleTest() {
		command.initialize();
		command.execute();
	}

	
	@Test
	public void checkStopLeaning() {
		lean.leanRight();
		assertEquals(-0.2, lean.motor.getMotorOutputPercent(), 0.001);
		command.initialize();
		command.execute();
		assertEquals(0.0, lean.motor.getMotorOutputPercent(), 0.001);
	}
}
