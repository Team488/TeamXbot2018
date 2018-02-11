package competition.subsystems.wrist.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.wrist.WristSubsystem;
import competition.subsystems.wrist.commands.WristDownCommand;

public class WristDownCommandTest extends BaseCompetitionTest {

    WristSubsystem wrist;
    WristDownCommand command;

    @Override
    public void setUp() {
        super.setUp();

        wrist = injector.getInstance(WristSubsystem.class);
        command = injector.getInstance(WristDownCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkDeployDown() {
        assertEquals(0.0, wrist.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(-1, wrist.motor.getMotorOutputPercent(), 0.001);
    }
}
