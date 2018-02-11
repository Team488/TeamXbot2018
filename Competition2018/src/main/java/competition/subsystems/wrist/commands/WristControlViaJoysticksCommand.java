package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristControlViaJoysticksCommand extends BaseCommand {
    
    final WristSubsystem gripperDeploySubsystem;
    final OperatorInterface oi;
    
    @Inject
    public WristControlViaJoysticksCommand (WristSubsystem gripperDeploySubsystem, OperatorInterface oi) {
        this.oi = oi;
        this.gripperDeploySubsystem = gripperDeploySubsystem;
        this.requires(gripperDeploySubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute() {
        gripperDeploySubsystem.motor.simpleSet(oi.operatorGamepad.getLeftVector().y);
    }
}
