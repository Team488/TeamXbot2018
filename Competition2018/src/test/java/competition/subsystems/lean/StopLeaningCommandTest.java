package competition.subsystems.lean;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.StopLeaningCommand;

public class StopLeaningCommandTest extends BaseCompetitionTest {

    LeanSubsystem lean;
    StopLeaningCommand command;

    @Override
    public void setUp() {
        super.setUp();

        lean = injector.getInstance(LeanSubsystem.class);
        command = injector.getInstance(StopLeaningCommand.class);

        lean.temporaryHack();
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkStopLeaning() {
        lean.leanRight();
        assertEquals(-0.4, lean.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(0.0, lean.motor.getMotorOutputPercent(), 0.001);
    }
}
