package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.MockTimer;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.controls.sensors.TalonCurrentMonitor;

public class CalibrateElevatorViaStallCommandTest extends BaseCompetitionTest {

    ElevatorSubsystem elevator;
    CalibrateElevatorViaStallCommand command;
    TalonCurrentMonitor currentMonitor;
    XCANTalon talon;
    MockTimer mockTimer;

    double current;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(CalibrateElevatorViaStallCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
        currentMonitor = new TalonCurrentMonitor(elevator.motor);
        mockTimer = injector.getInstance(MockTimer.class);
    }

    @Test
    public void calibrationCurrentWithPeakCurrentTest() {

        command.calibrationCurrentThreshold.set(command.currentThreshold);
        command.initialize();

        ((MockCANTalon) elevator.motor).setOutputCurrent(command.currentThreshold - 5);
        command.execute();
        assertFalse(command.isFinished());
        ((MockCANTalon) elevator.motor).setOutputCurrent(command.currentThreshold);
        command.execute();
        assertFalse(command.isFinished());
        ((MockCANTalon) elevator.motor).setOutputCurrent(command.currentThreshold + 5);
        command.execute();
        assertTrue(command.isFinished());

    }

    @Test
    public void calibrationCurrentWithTimeTest() {
        command.initialize();

        ((MockCANTalon) elevator.motor).setOutputCurrent(14);
        mockTimer.setTimeInSeconds(0);
        command.execute();
        assertFalse(command.isFinished());
        mockTimer.setTimeInSeconds(command.targetTime - 1);
        command.execute();
        assertFalse(command.isFinished());
        mockTimer.setTimeInSeconds(command.targetTime);
        command.execute();
        assertFalse(command.isFinished());
        mockTimer.setTimeInSeconds(command.targetTime + 1);
        command.execute();
        assertTrue(command.isFinished());

    }

}