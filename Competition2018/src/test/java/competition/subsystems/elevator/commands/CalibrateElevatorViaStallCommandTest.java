package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.controls.sensors.TalonCurrentMonitor;


public class CalibrateElevatorViaStallCommandTest extends BaseCompetitionTest{

    ElevatorSubsystem elevator;
    CalibrateElevatorViaStallCommand command;
    TalonCurrentMonitor currentMonitor;
    XCANTalon talon;

    double current;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(CalibrateElevatorViaStallCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
        currentMonitor = injector.getInstance(currentMonitor.getClass());
        talon = clf.createCANTalon(1);
        currentMonitor = new TalonCurrentMonitor(talon);
    }
    @Test
    public void calibrationCurrentTest() {

        command.setCalibrationCurrentThreshold(20);
        for (int i = 1; i <= 24; i++) {
            ((MockCANTalon) talon).setOutputCurrent(i);
        }
        command.initialize();
        command.execute();
        assertTrue(/*command.isFinished() == true ||*/ elevator.getPeakCurrent() <= 20);
    }

}
