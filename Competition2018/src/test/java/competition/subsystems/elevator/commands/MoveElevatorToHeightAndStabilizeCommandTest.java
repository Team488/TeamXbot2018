package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.MockTimer;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class MoveElevatorToHeightAndStabilizeCommandTest extends BaseCompetitionTest {
    ElevatorSubsystem elevator;
    MoveElevatorToHeightAndStabilizeCommand command;
    MockTimer mockTimer;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(MoveElevatorToHeightAndStabilizeCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
        mockTimer = injector.getInstance(MockTimer.class);
    }

    @Test
    public void testCommand() {
        command.setTargetHeight(80);
        command.initialize();
        mockTimer.setTimeInSeconds(0);

        // at target height and required time met --> should be true
        ((MockCANTalon) elevator.master).setPosition(7700);
        command.execute();
        mockTimer.advanceTimeInSecondsBy(command.stableTimeRequired.get());
        assertTrue(command.isFinished());

        // required time for meeting target met but not at target --> should be false
        ((MockCANTalon) elevator.master).setPosition(20);
        command.execute();
        mockTimer.advanceTimeInSecondsBy(command.stableTimeRequired.get());
        assertFalse(command.isFinished());

        // required time for meeting target not met but at target --> should be false
        mockTimer.advanceTimeInSecondsBy(command.stableTimeRequired.get() - 0.3);
        ((MockCANTalon) elevator.master).setPosition(7700);
        command.execute();
        assertFalse(command.isFinished());
    }

}
