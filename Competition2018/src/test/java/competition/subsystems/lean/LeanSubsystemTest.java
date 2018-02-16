package competition.subsystems.lean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj.MockDigitalInput;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class LeanSubsystemTest extends BaseCompetitionTest {
     
    LeanSubsystem lean;
    
    @Override
    public void setUp() {
        super.setUp();
        this.lean = injector.getInstance(LeanSubsystem.class);
    }
    
    @Test
    public void isStopped() {
        lean.leanLeft();
        assertEquals(.4, lean.getLeanSpeed(), .01);
        lean.stopLean();
        assertEquals(0, lean.getLeanSpeed(), .01);
    }
    
    @Test
    public void leanLeft() {
        lean.stopLean();
        assertEquals(0, lean.getLeanSpeed(), .01);
        lean.leanLeft();
        assertEquals(.4, lean.getLeanSpeed(), .01);
    }
    
    @Test
    public void leanRight() {
        lean.stopLean();
        assertEquals(0, lean.getLeanSpeed(), .01);
        lean.leanRight();
        assertEquals(-.4, lean.getLeanSpeed(), .01);
    }
    
    @Test
    public void setLeanSpeed() {
        lean.setLeanSpeed(.7);
        lean.leanLeft();
        assertEquals(.7, lean.getLeanSpeed(), .01);
        lean.setLeanSpeed(.2);
        lean.leanLeft();
        assertEquals(.2, lean.getLeanSpeed(), .01);
    }
    
    @Test
    public void increaseSpeed() {
        lean.setLeanSpeed(.3);
        lean.increaseSpeed();
        lean.leanLeft();
        assertEquals(.4, lean.getLeanSpeed(), .01);
    }
    
    @Test
    public void decreaseSpeed() {
        lean.setLeanSpeed(.3);
        lean.decreaseSpeed();
        lean.leanLeft();
        assertEquals(.1, lean.getLeanSpeed(), .01);
    }
}
