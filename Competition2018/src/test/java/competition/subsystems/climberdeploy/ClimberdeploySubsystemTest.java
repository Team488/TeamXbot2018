package competition.subsystems.climberdeploy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;


public class ClimberdeploySubsystemTest extends BaseCompetitionTest {

    ClimberDeploySubsystem climberdeploy;
    
    @Override
    public void setUp() {
        super.setUp();
        this.climberdeploy = injector.getInstance(ClimberDeploySubsystem.class);
    }
    
    @Test
    public void isStopped() {
        climberdeploy.extendClimberArm();
        assertEquals(.4, climberdeploy.getCurrentSpeed(), .01);
        climberdeploy.stopClimberArm();
        assertEquals(.0, climberdeploy.getCurrentSpeed(), .01);
    }
    
    @Test
    public void isExtending() {
        climberdeploy.stopClimberArm();
        assertEquals(.0, climberdeploy.getCurrentSpeed(), .01);
        climberdeploy.extendClimberArm();
        assertEquals(.4, climberdeploy.getCurrentSpeed(), .01);
    }
    
    @Test
    public void isRetracting() {
        climberdeploy.stopClimberArm();
        assertEquals(.0, climberdeploy.getCurrentSpeed(), .01);
        climberdeploy.retractClimberArm();
        assertEquals(-.4, climberdeploy.getCurrentSpeed(), .01);
    }
    
    @Test
    public void isSpeedDecreasing() {
        climberdeploy.increaseSpeed();
        climberdeploy.extendClimberArm();
        assertEquals(.4, climberdeploy.getCurrentSpeed(), .01);
        climberdeploy.decreaseSpeed();
        climberdeploy.extendClimberArm();
        assertEquals(.1, climberdeploy.getCurrentSpeed(), .01);
    }
    
    @Test
    public void isSpeedIncreasing() {
        climberdeploy.decreaseSpeed();
        climberdeploy.extendClimberArm();
        assertEquals(.1, climberdeploy.getCurrentSpeed(), .01);
        climberdeploy.increaseSpeed();
        climberdeploy.extendClimberArm();
        assertEquals(.4, climberdeploy.getCurrentSpeed(), .01);
    }
}
