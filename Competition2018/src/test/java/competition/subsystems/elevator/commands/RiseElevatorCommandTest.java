package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class RiseElevatorCommandTest extends BaseCompetitionTest {

    RiseElevatorCommand command;
    ElevatorSubsystem elevator;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(RiseElevatorCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
    }

    @Test
    public void testSimple() {

        command.initialize();
        command.execute();
    }

    @Test
    public void verifyMovingUp() {

        command.initialize();
        command.execute();

        assertTrue(elevator.motor.getMotorOutputPercent() >= 0.1);
    }
}
