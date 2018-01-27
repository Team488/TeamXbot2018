package competition.subsystems.lean.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.DecreaseSpeedCommand;
import xbot.common.injection.BaseWPITest;

public class DecreaseSpeedCommandTest extends BaseCompetitionTest {

    LeanSubsystem lean;
    DecreaseSpeedCommand command;

    @Override
    public void setUp() {
        super.setUp();

        lean = injector.getInstance(LeanSubsystem.class);
        command = injector.getInstance(DecreaseSpeedCommand.class);

        lean.temporaryHack();
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkDecreaseSpeed() {
        lean.leanRight();
        assertEquals(-0.4, lean.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        lean.leanRight();
        assertEquals(-0.1, lean.motor.getMotorOutputPercent(), 0.001);
    }
}
