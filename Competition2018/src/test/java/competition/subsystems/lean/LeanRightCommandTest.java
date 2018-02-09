package competition.subsystems.lean;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.LeanRightCommand;

public class LeanRightCommandTest extends BaseCompetitionTest {

    LeanSubsystem lean;
    LeanRightCommand command;

    @Override
    public void setUp() {
        super.setUp();

        lean = injector.getInstance(LeanSubsystem.class);
        command = injector.getInstance(LeanRightCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkLeanRight() {
        assertEquals(0.0, lean.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(-0.4, lean.motor.getMotorOutputPercent(), 0.001);
    }
}
