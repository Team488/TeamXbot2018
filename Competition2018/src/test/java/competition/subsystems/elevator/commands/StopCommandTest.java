package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class StopCommandTest extends BaseCompetitionTest {

    StopCommand command;
    ElevatorSubsystem elevator;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(StopCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
        elevator.temporaryHack();
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
