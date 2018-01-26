package competition.subsystems.lean.Commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.LeanLeftCommand;
import xbot.common.injection.BaseWPITest;

public class LeanLeftCommandTest extends BaseWPITest {

    LeanSubsystem lean;
    LeanLeftCommand command;

    @Override
    public void setUp() {
        super.setUp();

        lean = injector.getInstance(LeanSubsystem.class);
        command = injector.getInstance(LeanLeftCommand.class);

        lean.temporaryHack();
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkLeanLeft() {
        assertEquals(0.0, lean.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(0.4, lean.motor.getMotorOutputPercent(), 0.001);
    }
}
