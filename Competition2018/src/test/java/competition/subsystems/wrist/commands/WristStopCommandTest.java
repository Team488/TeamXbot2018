package competition.subsystems.wrist.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.wrist.WristSubsystem;
import competition.subsystems.wrist.commands.WristStopCommand;

public class WristStopCommandTest extends BaseCompetitionTest {

    WristSubsystem wrist;
    WristStopCommand command;

    @Override
    public void setUp() {
        super.setUp();

        wrist = injector.getInstance(WristSubsystem.class);
        command = injector.getInstance(WristStopCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkStopDeploy() {
        wrist.goUp();
        assertEquals(1, wrist.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(0.0, wrist.motor.getMotorOutputPercent(), 0.001);
    }
}
