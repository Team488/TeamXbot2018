package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperEjectCommandTest extends BaseCompetitionTest {

    GripperEjectCommand command;
    GripperIntakeSubsystem intake;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(GripperEjectCommand.class);
        intake = injector.getInstance(GripperIntakeSubsystem.class);
    }

    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }

    @Test
    public void verifyMovingOut() {

        command.initialize();
        command.execute();

        assertEquals(0.3, intake.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(0.3, intake.rightMotor.getMotorOutputPercent(), 0.001);
    }
}
