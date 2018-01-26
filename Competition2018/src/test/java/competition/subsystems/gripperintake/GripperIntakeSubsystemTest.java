package competition.subsystems.gripperintake;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class GripperIntakeSubsystemTest extends BaseCompetitionTest {
	
	GripperIntakeSubsystem intake;

	@Override
	public void setUp() {
		super.setUp();
		this.intake = injector.getInstance(GripperIntakeSubsystem.class);
		this.intake.temporaryHack();
	}
	
	@Test
	public void testEject() {
		assertEquals(intake.leftMotor.getMotorOutputPercent(), 0, 0.001);
		assertEquals(intake.rightMotor.getMotorOutputPercent(), 0, 0.001);
		intake.eject();
		assertTrue(intake.leftMotor.getMotorOutputPercent() > 0);
		assertTrue(intake.rightMotor.getMotorOutputPercent() > 0);
	}
	
	@Test
	public void testIntake() {
		assertEquals(intake.leftMotor.getMotorOutputPercent(), 0, 0.001);
		assertEquals(intake.rightMotor.getMotorOutputPercent(), 0, 0.001);
		intake.intake();
		assertTrue(intake.leftMotor.getMotorOutputPercent() < 0);
		assertTrue(intake.rightMotor.getMotorOutputPercent() < 0);
	}
	
	@Test
	public void testLeftDominant() {
		assertEquals(intake.leftMotor.getMotorOutputPercent(), 0, 0.001);
		assertEquals(intake.rightMotor.getMotorOutputPercent(), 0, 0.001);
		intake.intakeleftDominant();
		assertTrue(Math.abs(intake.leftMotor.getMotorOutputPercent()) - Math.abs(intake.rightMotor.getMotorOutputPercent()) >= 0.1);
	}
	
	@Test
	public void testRightDominant() {
		assertEquals(intake.leftMotor.getMotorOutputPercent(), 0, 0.001);
		assertEquals(intake.rightMotor.getMotorOutputPercent(), 0, 0.001);
		intake.intakerightDominant();
		assertTrue(Math.abs(intake.rightMotor.getMotorOutputPercent()) - Math.abs(intake.leftMotor.getMotorOutputPercent()) >= 0.1);
	}
	
	@Test
	public void testStop() {
		assertEquals(intake.leftMotor.getMotorOutputPercent(), 0, 0.001);
		assertEquals(intake.rightMotor.getMotorOutputPercent(), 0, 0.001);
		intake.setPower(1, 1);
		intake.stop();
		assertTrue(intake.leftMotor.getMotorOutputPercent() == 0 );
		assertTrue(intake.rightMotor.getMotorOutputPercent() == 0);
	}
}
