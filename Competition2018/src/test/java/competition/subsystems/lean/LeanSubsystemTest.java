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
        lean.setPower(.4);
        assertEquals(.4, lean.getPower(), .01);
        lean.setPower(0.0);
        assertEquals(0, lean.getPower(), .01);
    }

    @Test
    public void leanLeft() {
        lean.setPower(0.0);
        assertEquals(0, lean.getPower(), .01);
        lean.setPower(.4);
        assertEquals(.4, lean.getPower(), .01);
    }

    @Test
    public void leanRight() {
        lean.setPower(0.0);
        assertEquals(0, lean.getPower(), .01);
        lean.setPower(-.4);
        assertEquals(-.4, lean.getPower(), .01);
    }
}
