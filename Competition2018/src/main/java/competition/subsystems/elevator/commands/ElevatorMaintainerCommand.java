package competition.subsystems.elevator.commands;

import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDFactory;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;

import com.google.inject.Inject;
import xbot.common.math.PIDManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.operator_interface.OperatorInterface;

public class ElevatorMaintainerCommand extends BaseCommand {

    public enum MaintinerMode {
        Calibrating, GaveUp, Calibrated
    }

    OperatorInterface oi;
    ElevatorSubsystem elevator;
    double giveUpCalibratingTime;
    final BooleanProperty motionMagicEnabled;
    final DoubleProperty elevatorCalibrationAttemptTimeMS;
    double throttle;

    @Inject
    public ElevatorMaintainerCommand(ElevatorSubsystem elevator, PIDFactory pf, XPropertyManager propMan,
            OperatorInterface oi) {
        elevatorCalibrationAttemptTimeMS = propMan
                .createPersistentProperty(getPrefix() + "Calibration attempt time (ms)", 4000);
        motionMagicEnabled = propMan.createPersistentProperty(getPrefix() + "Motion Magic Enabled", false);
        this.elevator = elevator;
        this.requires(elevator);
        this.oi = oi;
    }

    @Override
    public void initialize() {
        elevator.configureMotionMagic();
        log.info("Initializing");
        if (!elevator.isCalibrated()) {
            log.warn("ELEVATOR UNCALIBRATED - THIS COMMAND WILL NOT DO ANYTHING!");
            giveUpCalibratingTime = Timer.getFPGATimestamp() + elevatorCalibrationAttemptTimeMS.get();
            log.info("Attempting calibration from " + Timer.getFPGATimestamp() + " until " + giveUpCalibratingTime);
        } else {
            log.info("Setting current height as target height");
            elevator.setTargetHeight(elevator.getCurrentHeightInInches());
        }
    }

    @Override
    public void execute() {
        MaintinerMode currentMode = MaintinerMode.Calibrating;

        if (elevator.isCalibrated()) {
            currentMode = MaintinerMode.Calibrated;
        } else if (Timer.getFPGATimestamp() < giveUpCalibratingTime) {
            currentMode = MaintinerMode.Calibrating;
        } else {
            currentMode = MaintinerMode.GaveUp;
        }

        if (currentMode == MaintinerMode.Calibrated) {
            if (motionMagicEnabled.get() == true) {
                elevator.motionMagicToHeight(elevator.getTargetHeight());
            } else {
                double positionOutput = elevator.getPositionalPid().calculate(elevator.getTargetHeight(), elevator.getCurrentHeightInInches());
                double powerDelta = elevator.getVelocityPid().calculate(positionOutput*16, elevator.getVelocityInchesPerSecond());
                throttle += powerDelta;
                throttle = MathUtils.constrainDouble(throttle, -0.2, 1);
                elevator.setPower(throttle);
            }
        } else if (currentMode == MaintinerMode.Calibrating) {
            elevator.lower();
        } else if (currentMode == MaintinerMode.GaveUp) {
            elevator.setPower(oi.operatorGamepad.getRightStickY());
        }
    }

    public void isMotionMagicMode(boolean x) {
        motionMagicEnabled.set(x);
    }
}
