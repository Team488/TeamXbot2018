package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristDangerousOverrideCommand extends BaseCommand {
    WristSubsystem wrist;
    OperatorInterface oi;
    
    @Inject
    public WristDangerousOverrideCommand(WristSubsystem wrist, OperatorInterface oi) {
        this.wrist = wrist;
        this.oi = oi;
        this.requires(wrist);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        wrist.insanelyDangerousSetPower(oi.driverGamepad.getLeftVector().y);
    }
    
}
