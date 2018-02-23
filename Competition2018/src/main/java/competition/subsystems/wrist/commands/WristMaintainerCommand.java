package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class WristMaintainerCommand extends BaseCommand {

    WristSubsystem wrist;
    PIDManager pid;
    
    @Inject
    public WristMaintainerCommand(WristSubsystem wrist, PIDFactory pf) {
        this.wrist = wrist;
        this.requires(wrist);
        pid = pf.createPIDManager(getPrefix()+"motor", 0.01, 0, 0);
        pid.setMaxOutput(0.3);
        pid.setMinOutput(-0.3);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (!wrist.getIsCalibrated()) {
            // If the wrist isn't calibrated, it's not safe to do anything.
            wrist.stop();
        } else {
            double power = pid.calculate(wrist.getTargetAngle(), wrist.getWristAngle());
            wrist.setPower(power);
        }
    }
    
}
