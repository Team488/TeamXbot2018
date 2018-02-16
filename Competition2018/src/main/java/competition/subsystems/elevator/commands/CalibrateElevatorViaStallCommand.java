package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.TalonCurrentMonitor;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class CalibrateElevatorViaStallCommand extends BaseCommand {
    final ElevatorSubsystem elevator;
    CommonLibFactory clf;
    TalonCurrentMonitor currentMonitor;

    DoubleProperty power;
    DoubleProperty calibrationTime; // In milliseconds
    DoubleProperty calibrationCurrentThreshold;
    DoubleProperty goodCurrentRange;

    double targetTime;
    double newTime;
    boolean atTarget;
    double threshold;
    double peakCurrent;


    @Inject
    public CalibrateElevatorViaStallCommand(XPropertyManager propMan, CommonLibFactory clf, ElevatorSubsystem elevator) {
        this.clf = clf;
        power = propMan.createPersistentProperty("Elevator Jamming Calibration Power", -0.2);
        calibrationTime = propMan.createPersistentProperty("Elevator Calibration time (ms)", 4000);
        calibrationCurrentThreshold = propMan.createPersistentProperty("Calibration Current Threshold", 15);
        this.elevator = elevator;
        this.currentMonitor = new TalonCurrentMonitor(elevator.motor);
        this.requires(elevator);
    }
    
    
    public void setCalibrationCurrentThreshold(double threshold) {
        this.threshold=threshold;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        log.info("Current time is: " + Timer.getFPGATimestamp());
        log.info("Calibrating elevator at power" + power.get() + " until: " + targetTime);
        targetTime = Timer.getFPGATimestamp() + calibrationTime.get();
    }

    @Override
    public void execute() {
            elevator.setPower(power.get());
            currentMonitor.updateCurrent();     
    }
    
    @Override
    public boolean isFinished() {
        newTime =Timer.getFPGATimestamp();
        return newTime > targetTime
                || elevator.getPeakCurrent() > threshold;
    }

    @Override
    public void end() {
        elevator.stop();
    }

}
