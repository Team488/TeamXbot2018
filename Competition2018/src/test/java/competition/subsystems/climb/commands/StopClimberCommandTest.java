package competition.subsystems.climb.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climb.ClimbSubsystem;
import competition.subsystems.climb.commands.StopClimberCommand;

public class StopClimberCommandTest extends BaseCompetitionTest {

    ClimbSubsystem climb;
    StopClimberCommand command;

    @Override
    public void setUp() {
        super.setUp();
        climb = injector.getInstance(ClimbSubsystem.class);
        command = injector.getInstance(StopClimberCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkStopClimber() {
        climb.ascend();
        assertEquals(1, climb.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(0, climb.motor.getMotorOutputPercent(), 0.001);
    }

}
