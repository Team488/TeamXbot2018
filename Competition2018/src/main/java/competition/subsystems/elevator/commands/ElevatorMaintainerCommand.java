package competition.subsystems.elevator.commands;

import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.HumanVsMachineDecider;
import xbot.common.logic.HumanVsMachineDecider.HumanVsMachineMode;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDFactory;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;

import com.google.inject.Inject;
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
    HumanVsMachineDecider decider;

    @Inject
    public ElevatorMaintainerCommand(ElevatorSubsystem elevator, PIDFactory pf, XPropertyManager propMan,
            OperatorInterface oi, CommonLibFactory clf) {
        elevatorCalibrationAttemptTimeMS = propMan
                .createPersistentProperty(getPrefix() + "Calibration attempt time (ms)", 4000);
        motionMagicEnabled = propMan.createPersistentProperty(getPrefix() + "Motion Magic Enabled", false);
        this.elevator = elevator;
        this.requires(elevator);
        this.oi = oi;
        decider = clf.createHumanVsMachineDecider("Elevator");
    }

    @Override
    public void initialize() {
        elevator.configureMotionMagic();
        log.info("Initializing with distance " + elevator.getTargetHeight() + " inches");
        if (!elevator.isCalibrated()) {
            log.warn("ELEVATOR UNCALIBRATED - THIS COMMAND WILL NOT DO ANYTHING!");
            giveUpCalibratingTime = Timer.getFPGATimestamp() + elevatorCalibrationAttemptTimeMS.get();
            log.info("Attempting calibration from " + Timer.getFPGATimestamp() + " until " + giveUpCalibratingTime);
        } else {
            log.info("Setting current height (" + elevator.getCurrentHeightInInches() + " inches) as target height");
            elevator.setTargetHeight(elevator.getCurrentHeightInInches());
        }
        
        decider.reset();
    }

    @Override
    public void execute() {
        MaintinerMode currentMode = MaintinerMode.Calibrating;

        // Decide what meta-level activity the elevator is involved in
        // If the elevator is uncalibrated, it will try to calibrate.
        // If this takes too long, it gives up, and we are in 100% human control
        // Otherwise, we are in the traditional maintainer mode.
        if (elevator.isCalibrated()) {
            currentMode = MaintinerMode.Calibrated;
        } else if (Timer.getFPGATimestamp() < giveUpCalibratingTime) {
            currentMode = MaintinerMode.Calibrating;
        } else {
            currentMode = MaintinerMode.GaveUp;
        }
        
        // Decide what low-level activity the elevator is involved in.
        // Will only be used if the elevator is calibrated.
        double humanInput = oi.operatorGamepad.getRightStickY();
        HumanVsMachineMode deciderMode = decider.getRecommendedMode(humanInput);
        double power = 0;
        

        if (currentMode == MaintinerMode.Calibrated) {
            switch (deciderMode) {
            case HumanControl:
                power = humanInput;
                break;
            case Coast:
                power = 0;
                break;
            case InitializeMachineControl:
                power = 0;
                elevator.setTargetHeight(elevator.getCurrentHeightInInches());
                break;
            case MachineControl:
                if (motionMagicEnabled.get() == true) {
                    elevator.motionMagicToHeight(elevator.getTargetHeight());
                    return;
                } else {
                    double positionOutput = elevator.getPositionalPid().calculate(elevator.getTargetHeight(), elevator.getCurrentHeightInInches());
                    double powerDelta = elevator.getVelocityPid().calculate(positionOutput*30, elevator.getVelocityInchesPerSecond());
                    throttle += powerDelta;
                    throttle = MathUtils.constrainDouble(throttle, -0.2, 1);
                    power = throttle;
                }
                break;
            default: 
                power = 0;
                break;
            }
            elevator.setPower(power);
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
