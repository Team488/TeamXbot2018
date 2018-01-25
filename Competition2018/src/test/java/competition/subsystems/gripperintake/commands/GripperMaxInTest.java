package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperMaxInTest extends BaseCompetitionTest {
	
	GripperIntake command;
	GripperIntakeSubsystem intake;
	
	@Override
	public void setUp() {
		super.setUp();
		
		command = injector.getInstance(GripperIntake.class);
		intake = injector.getInstance(GripperIntakeSubsystem.class);
		intake.temporaryHack();
	}
	
	@Test
	public void testSimple() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void verifyMovingIn() {
		
		command.initialize();
		command.execute();
		
		assertTrue(intake.leftMotor.getMotorOutputPercent() <= -1);
		assertTrue(intake.rightMotor.getMotorOutputPercent() <= -1);
	}

}
