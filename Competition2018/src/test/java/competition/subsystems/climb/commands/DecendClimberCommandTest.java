package competition.subsystems.climb.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climb.ClimbSubsystem;
import competition.subsystems.climb.commands.DecendClimberCommand;

public class DecendClimberCommandTest extends BaseCompetitionTest {

    ClimbSubsystem climb;
    DecendClimberCommand command;

    @Override
    public void setUp() {
        super.setUp();
        climb = injector.getInstance(ClimbSubsystem.class);
        command = injector.getInstance(DecendClimberCommand.class);
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
