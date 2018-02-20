package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseSetpointCommand;
import xbot.common.math.MathUtils;

public class SetWristAngleCommand extends BaseSetpointCommand {

    WristSubsystem wrist;
    double goalAngle;
    
    @Inject
    public SetWristAngleCommand(WristSubsystem wrist) {
        super(wrist);
        this.wrist = wrist;
    }
    
    public void setGoalAngle(double angle) {
        goalAngle = MathUtils.constrainDouble(angle, 0, 90);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        log.info("Setting Wrist target angle to " + goalAngle);
        wrist.setTargetAngle(goalAngle);
    }

    @Override
    public void execute() {
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }

}
