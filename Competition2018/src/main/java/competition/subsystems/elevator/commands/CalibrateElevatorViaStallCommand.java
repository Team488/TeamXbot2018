package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.TalonCurrentMonitor;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class CalibrateElevatorViaStallCommand extends BaseCommand {
    final ElevatorSubsystem elevator;
    TalonCurrentMonitor currentMonitor;

    DoubleProperty calibrationTime; 
    DoubleProperty calibrationCurrentThreshold;
    DoubleProperty goodCurrentRange;

    double targetTime;
    boolean atTarget;
    double currentThreshold;
    double peakCurrent;


    @Inject
    public CalibrateElevatorViaStallCommand(XPropertyManager propMan, ElevatorSubsystem elevator) {
        calibrationTime = propMan.createPersistentProperty("Elevator Calibration time (s)", 4);
        calibrationCurrentThreshold = propMan.createPersistentProperty("Calibration Current Threshold", 15);
        this.elevator = elevator;
        this.currentMonitor = new TalonCurrentMonitor(elevator.master);
        this.requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        log.info("Current time is: " + Timer.getFPGATimestamp());
        this.currentThreshold = calibrationCurrentThreshold.get();
        targetTime = Timer.getFPGATimestamp() + calibrationTime.get();
    }

    @Override
    public void execute() {
        elevator.lower();
        currentMonitor.updateCurrent();
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() > targetTime
                || currentMonitor.calculatePeakCurrent() > currentThreshold;
    }

    @Override
    public void end() {
        elevator.calibrateHere();
        elevator.stop();
    }

}
