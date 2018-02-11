package competition.subsystems.wrist.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.wrist.WristSubsystem;
import competition.subsystems.wrist.commands.WristUpCommand;

public class WristUpCommandTest extends BaseCompetitionTest {

    WristSubsystem wrist;
    WristUpCommand command;

    @Override
    public void setUp() {
        super.setUp();

        wrist = injector.getInstance(WristSubsystem.class);
        command = injector.getInstance(WristUpCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkDeployUp() {
        assertEquals(0.0, wrist.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(1, wrist.motor.getMotorOutputPercent(), 0.001);
    }
}