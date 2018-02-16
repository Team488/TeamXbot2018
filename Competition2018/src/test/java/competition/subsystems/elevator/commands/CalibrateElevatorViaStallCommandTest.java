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
        talon = clf.createCANTalon(1);
        // mockTimer = injector.getInstance(MockTimer.class);
    }

    @Test
    public void calibrationCurrentTest() {

        command.setCalibrationCurrentThreshold(20);
        command.initialize();
        
        ((MockCANTalon) talon).setOutputCurrent(14);
        command.execute();
        assertFalse(command.isFinished());
        ((MockCANTalon) talon).setOutputCurrent(20);
        command.execute();
        assertFalse(command.isFinished());
        ((MockCANTalon) talon).setOutputCurrent(24);
        command.execute();
        assertTrue(command.isFinished());

        /* || command.newTime> command.targetTime); */
    }

}