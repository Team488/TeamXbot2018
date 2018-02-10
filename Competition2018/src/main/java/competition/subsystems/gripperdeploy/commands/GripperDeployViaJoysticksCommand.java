package competition.subsystems.gripperdeploy.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import xbot.common.command.BaseCommand;

public class GripperDeployViaJoysticksCommand extends BaseCommand {
    
    final GripperDeploySubsystem gripperDeploySubsystem;
    final OperatorInterface oi;
    
    @Inject
    public GripperDeployViaJoysticksCommand (GripperDeploySubsystem gripperDeploySubsystem, OperatorInterface oi) {
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
