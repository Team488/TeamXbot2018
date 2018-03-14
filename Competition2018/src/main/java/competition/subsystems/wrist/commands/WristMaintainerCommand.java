package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.CalibrationDecider;
import xbot.common.logic.CalibrationDecider.CalibrationMode;
import xbot.common.logic.HumanVsMachineDecider;
import xbot.common.logic.HumanVsMachineDecider.HumanVsMachineMode;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class WristMaintainerCommand extends BaseCommand {

    final WristSubsystem wrist;
    final PIDManager pid;
    final OperatorInterface oi;
    final HumanVsMachineDecider humanVsMachineDecider;
    final CalibrationDecider calibrationDecider;

    @Inject
    public WristMaintainerCommand(WristSubsystem wrist, PIDFactory pf, OperatorInterface oi, CommonLibFactory clf) {
        this.wrist = wrist;
        this.oi = oi;
        this.requires(wrist);
        pid = pf.createPIDManager(getPrefix() + "motor", 0.01, 0, 0);
        pid.setMaxOutput(0.3);
        pid.setMinOutput(-0.3);
        this.humanVsMachineDecider = clf.createHumanVsMachineDecider(getPrefix() + "Wrist");
        this.calibrationDecider = clf.createCalibrationDecider(getPrefix() + "Wrist");
    }

    @Override
    public void initialize() {
        log.info("Initializing with distance " + wrist.getTargetAngle() + " degrees");

        if (wrist.getIsCalibrated()) {
            log.info("Setting current angle as desired angle");
            wrist.setTargetAngle(wrist.getWristAngle());
        }

        humanVsMachineDecider.reset();
        calibrationDecider.reset();
    }

    @Override
    public void execute() {
        HumanVsMachineMode humanVsMachineMode = HumanVsMachineMode.HumanControl;
        double humanInput = oi.operatorGamepad.getLeftVector().y;
        double power = 0;
        /*
        CalibrationMode calibrationMode = calibrationDecider.decideMode(wrist.getIsCalibrated());

        if (calibrationMode == CalibrationMode.GaveUp) {
            // When we've totally given up, give the drivers control. Good luck.
            humanVsMachineMode = HumanVsMachineMode.HumanControl;
        } else if (calibrationMode == CalibrationMode.Attempting) {
            // The wrist already restrains power if system uncalibrated, so we just need to raise it with any large
            // power.
            wrist.setPower(1);
            // We've already decided what to do, so just return.
            return;
        } else {
            // We're calibrated, so let the HvM figure out the best course of action.
            humanVsMachineMode = humanVsMachineDecider.getRecommendedMode(humanInput);
        } */
        
        humanVsMachineMode = humanVsMachineDecider.getRecommendedMode(humanInput);

        switch (humanVsMachineMode) {
        case HumanControl:
            power = humanInput;
            break;
        case Coast:
            power = 0;
            break;
        case InitializeMachineControl:
            power = 0;
            wrist.setTargetAngle(wrist.getWristAngle());
            break;
        case MachineControl:
            power = pid.calculate(wrist.getTargetAngle(), wrist.getWristAngle());
            break;
        default:
            break;
        }

        wrist.setPower(power);
    }

}
