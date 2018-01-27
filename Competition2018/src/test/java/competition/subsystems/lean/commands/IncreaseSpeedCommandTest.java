package competition.subsystems.lean.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.IncreaseSpeedCommand;
import xbot.common.injection.BaseWPITest;

public class IncreaseSpeedCommandTest extends BaseCompetitionTest {

    LeanSubsystem lean;
    IncreaseSpeedCommand command;

    @Override
    public void setUp() {
        super.setUp();

        lean = injector.getInstance(LeanSubsystem.class);
        command = injector.getInstance(IncreaseSpeedCommand.class);

        lean.temporaryHack();
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkIncreaseSpeed() {
        lean.decreaseSpeed();
        lean.leanRight();
        assertEquals(-0.1, lean.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        lean.leanRight();
        assertEquals(-0.4, lean.motor.getMotorOutputPercent(), 0.001);
    }
}
