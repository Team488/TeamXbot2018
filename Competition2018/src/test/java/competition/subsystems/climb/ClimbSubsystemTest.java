package competition.subsystems.climb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class ClimbSubsystemTest extends BaseCompetitionTest {

    ClimbSubsystem climbSubsystem;

    @Override
    public void setUp() {
        super.setUp();
        climbSubsystem = injector.getInstance(ClimbSubsystem.class);
    }

    @Test
    public void testAscend() {
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() == 0);
        climbSubsystem.ascend();
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() > 0);
        
        climbSubsystem.decend();
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() < 0);
        climbSubsystem.ascend();
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() > 0);
    }

    @Test
    public void testDecend() {
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() == 0);
        climbSubsystem.decend();
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() < 0);
        
        climbSubsystem.ascend();
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() > 0);
        climbSubsystem.decend();
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() < 0);
    }

    @Test
    public void testStop() {
        climbSubsystem.ascend();
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() > 0);
        climbSubsystem.stop();
        assertEquals(0, climbSubsystem.motor.getMotorOutputPercent(), 0.00001);
        
        climbSubsystem.decend();
        assertTrue(climbSubsystem.motor.getMotorOutputPercent() < 0);
        climbSubsystem.stop();
        assertEquals(0, climbSubsystem.motor.getMotorOutputPercent(), 0.00001);
    }

}
