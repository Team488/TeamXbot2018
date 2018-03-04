package competition.subsystems.climb.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climb.ClimbSubsystem;
import competition.subsystems.climb.commands.DescendClimberCommand;

public class DecendClimberCommandTest extends BaseCompetitionTest {

    ClimbSubsystem climb;
    DescendClimberCommand command;

    @Override
    public void setUp() {
        super.setUp();
        climb = injector.getInstance(ClimbSubsystem.class);
        command = injector.getInstance(DescendClimberCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    // TODO: UNHACKIFY
    /*
    @Test
    public void checkDecendClimber() {
        assertEquals(0.0, climb.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(-.1, climb.motor.getMotorOutputPercent(), 0.001);
    }*/

}
