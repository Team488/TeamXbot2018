package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperMaxOutTest extends BaseCompetitionTest {

	GripperEject command;
	GripperIntakeSubsystem intake;
	
	@Override
	public void setUp() {
		super.setUp();
		
		command = injector.getInstance(GripperEject.class);
		intake = injector.getInstance(GripperIntakeSubsystem.class);
		intake.temporaryHack();
	}
	
	@Test
	public void testSimple() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void verifyMovingOut() {
		
		command.initialize();
		command.execute();
		
		assertTrue(intake.leftMotor.getMotorOutputPercent() >= 1);
		assertTrue(intake.rightMotor.getMotorOutputPercent() >= 1);
	}
}
