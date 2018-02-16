package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class StopElevatorCommandTest extends BaseCompetitionTest {

    StopElevatorCommand command;
    ElevatorSubsystem elevator;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(StopElevatorCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
    }

    @Test
    public void testSimple() {
        command.execute();
    }

    @Test
    public void verifyMovingUp() {
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() == 0);
    }
}
