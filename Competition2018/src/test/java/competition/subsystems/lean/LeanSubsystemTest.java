package competition.subsystems.lean;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;

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
