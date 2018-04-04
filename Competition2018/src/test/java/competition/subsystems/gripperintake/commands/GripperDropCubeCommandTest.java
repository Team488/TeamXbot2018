package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperDropCubeCommandTest extends BaseCompetitionTest {
    
    GripperDropCubeCommand command;
    GripperIntakeSubsystem intake;
    
    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(GripperDropCubeCommand.class);
        intake = injector.getInstance(GripperIntakeSubsystem.class);
    }

    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }

    @Test
    public void verifyDropping() {

        command.initialize();
        command.execute();

        assertEquals(command.dropPower.get(), intake.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(command.dropPower.get(), intake.rightMotor.getMotorOutputPercent(), 0.001);
    }
}
