package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristControlViaJoysticksCommand extends BaseCommand {
    
    final WristSubsystem wrist;
    final OperatorInterface oi;
    
    @Inject
    public WristControlViaJoysticksCommand (WristSubsystem wrist, OperatorInterface oi) {
        this.oi = oi;
        this.wrist = wrist;
        this.requires(wrist);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute() {
        wrist.motor.simpleSet(oi.operatorGamepad.getLeftVector().y);
    }
}
