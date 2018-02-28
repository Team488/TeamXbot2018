package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.HumanVsMachineDecider;
import xbot.common.logic.HumanVsMachineDecider.HumanVsMachineMode;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class WristMaintainerCommand extends BaseCommand {

    WristSubsystem wrist;
    PIDManager pid;
    OperatorInterface oi;
    HumanVsMachineDecider decider;
    
    @Inject
    public WristMaintainerCommand(WristSubsystem wrist, PIDFactory pf, OperatorInterface oi, CommonLibFactory clf) {
        this.wrist = wrist;
        this.oi = oi;
        this.requires(wrist);
        pid = pf.createPIDManager(getPrefix()+"motor", 0.01, 0, 0);
        pid.setMaxOutput(0.3);
        pid.setMinOutput(-0.3);
        this.decider = clf.createHumanVsMachineDecider(getPrefix() + "Wrist");
    }

    @Override
    public void initialize() {
        log.info("Initializing with distance " + wrist.getTargetAngle() + " degrees");
        
        if (wrist.getIsCalibrated()) {
            log.info("Setting current angle as desired angle");
            wrist.setTargetAngle(wrist.getWristAngle());
        }
        
        decider.reset();
    }

    @Override
    public void execute() {
        HumanVsMachineMode mode = HumanVsMachineMode.HumanControl;
        double humanInput = oi.operatorGamepad.getLeftVector().y;
        double power = 0;
        
        if (!wrist.getIsCalibrated()) {
            mode = HumanVsMachineMode.HumanControl;
        } else {
            mode = decider.getRecommendedMode(humanInput);
        }
        
        switch (mode) {
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
