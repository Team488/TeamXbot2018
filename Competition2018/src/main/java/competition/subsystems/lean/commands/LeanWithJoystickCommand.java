package competition.subsystems.lean.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.lean.LeanSubsystem;
import xbot.common.command.BaseCommand;

public class LeanWithJoystickCommand extends BaseCommand {

    final LeanSubsystem leanSubsystem;
    final OperatorInterface oi;

    @Inject
    public LeanWithJoystickCommand(OperatorInterface oi, LeanSubsystem leanSubsystem) {
        this.oi = oi;
        this.leanSubsystem = leanSubsystem;
        this.requires(this.leanSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");

    }

    @Override
    public void execute() {
        double power = 0;
        if (oi.driverGamepad.getPOV() == 90) {
            power = .2;
        }
        if (oi.driverGamepad.getPOV() == 270) {
            power = -.2;
        }
        
        leanSubsystem.setPower(power);
    }
}
