package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class WristMaintainerCommand extends BaseCommand {
  
    WristSubsystem wrist;
    PIDManager pid;
    OperatorInterface oi;
    
    @Inject
    public WristMaintainerCommand(WristSubsystem wrist, PIDFactory pf, OperatorInterface oi) {
        this.wrist = wrist;
        this.oi = oi;
        this.requires(wrist);
        pid = pf.createPIDManager(getPrefix()+"motor", 0.01, 0, 0);
        pid.setMaxOutput(0.3);
        pid.setMinOutput(-0.3);
    }

    @Override
    public void initialize() {
        log.info("Initializing with distance " + wrist.getTargetAngle() + " degrees");
        
        if (wrist.getIsCalibrated()) {
            log.info("Setting current angle as desired angle");
            wrist.setTargetAngle(wrist.getWristAngle());
        }
      
    }

    @Override
    public void execute() {
        if (!wrist.getIsCalibrated()) {
            // If the wrist isn't calibrated, it's not safe to do anything automatic. Just listen to joysticks.
            // In the future, when we have a sensor, we can have a calibration routine here.
            wrist.setPower(oi.operatorGamepad.getLeftVector().y);
        } else {
            double power = pid.calculate(wrist.getTargetAngle(), wrist.getWristAngle());
            wrist.setPower(power);
        }
    }
    
}
