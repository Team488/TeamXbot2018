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
    public void testRotateClockwise() {
        assertEquals(intake.rightMotor.getMotorOutputPercent(), 0, 0.001);
        assertEquals(intake.leftMotor.getMotorOutputPercent(), 0, 0.001);
        intake.rotateClockwise();
        assertEquals(0, intake.rightMotor.getMotorOutputPercent() + intake.leftMotor.getMotorOutputPercent(), 0.001);
    }
    
    @Test
    public void testRotateCounterClockwise() {
        assertEquals(intake.rightMotor.getMotorOutputPercent(), 0, 0.001);
        assertEquals(intake.leftMotor.getMotorOutputPercent(), 0, 0.001);
        intake.rotateCounterClockwise();
        assertEquals(0, intake.rightMotor.getMotorOutputPercent() + intake.leftMotor.getMotorOutputPercent(), 0.001);
    }

    @Test
    public void testStop() {
        intake.setPower(1, 1);
        assertEquals(intake.leftMotor.getMotorOutputPercent(), 1, 0.001);
        assertEquals(intake.rightMotor.getMotorOutputPercent(), 1, 0.001);
        intake.stop();
        assertEquals(intake.leftMotor.getMotorOutputPercent(), 0, 0.001);
        assertEquals(intake.rightMotor.getMotorOutputPercent(), 0, 0.001);
    }
}
