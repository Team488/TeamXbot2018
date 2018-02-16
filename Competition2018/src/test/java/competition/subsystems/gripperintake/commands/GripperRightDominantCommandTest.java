package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperRightDominantCommandTest extends BaseCompetitionTest {

    GripperRightDominantCommand command;
    GripperIntakeSubsystem intake;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(GripperRightDominantCommand.class);
        intake = injector.getInstance(GripperIntakeSubsystem.class);
    }

    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }

    @Test
    public void verifyPower() {

        command.initialize();
        command.execute();

        assertTrue(intake.leftMotor.getMotorOutputPercent() < 0 && intake.leftMotor.getMotorOutputPercent() >= -0.25);
        assertTrue(intake.rightMotor.getMotorOutputPercent() >= -1);
    }
}