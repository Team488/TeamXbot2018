package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class CalibrateElevatorCommand extends BaseCommand {
    ElevatorSubsystem elevator;
    CommonLibFactory clf;

    DoubleProperty power;
    DoubleProperty calibrationTime; // In milliseconds

    double targetTime;
    boolean atTarget;

    @Inject
    public CalibrateElevatorCommand(XPropertyManager propMan, CommonLibFactory clf) {
        this.clf = clf;
        power = propMan.createPersistentProperty("Default Elevator Calibration Power", -0.2);
        calibrationTime = propMan.createPersistentProperty("Elevator Calibration time in milliseconds", 4000);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        targetTime = Timer.getFPGATimestamp() + calibrationTime.get();
    }

    @Override
    public void execute() {
        elevator.setPower(power.get());
    }

    @Override
    public boolean isFinished() {
        if (Timer.getFPGATimestamp() > targetTime) {
            end();
            return true;
        }
        return false;
    }

    @Override
    public void end() {
        elevator.stop();
    }

}
