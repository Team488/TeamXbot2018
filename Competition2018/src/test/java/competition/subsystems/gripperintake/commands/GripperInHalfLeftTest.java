package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import edu.wpi.first.wpilibj.MockXboxControllerAdapter;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class GripperInHalfLeftTest extends BaseCompetitionTest {

	GripperInHalfLeft command;
	GripperIntakeSubsystem intake;
	
	@Override
	public void setUp() {
		super.setUp();
		
		command = injector.getInstance(GripperInHalfLeft.class);
		intake = injector.getInstance(GripperIntakeSubsystem.class);
		intake.temporaryHack();
	}
	
	@Test
	public void testSimple() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void verifyPower() {
		
		command.initialize();
		command.execute();
		
		assertTrue(intake.leftMotor.getMotorOutputPercent() <= 0.25 && intake.leftMotor.getMotorOutputPercent() > 0);
		assertTrue(intake.rightMotor.getMotorOutputPercent() >= 1);
	}
}
