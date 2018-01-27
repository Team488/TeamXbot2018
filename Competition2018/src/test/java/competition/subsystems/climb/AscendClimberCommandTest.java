package competition.subsystems.climb;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climb.ClimbSubsystem;
import competition.subsystems.climb.commands.AscendClimberCommand;

public class AscendClimberCommandTest extends BaseCompetitionTest {

    ClimbSubsystem climb;
    AscendClimberCommand command;

    @Override
    public void setUp() {
        super.setUp();
        climb = injector.getInstance(ClimbSubsystem.class);
        command = injector.getInstance(AscendClimberCommand.class);
        climb.temporaryHack();
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkAscendClimber() {
        assertEquals(0.0, climb.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(1, climb.motor.getMotorOutputPercent(), 0.001);
    }

}
