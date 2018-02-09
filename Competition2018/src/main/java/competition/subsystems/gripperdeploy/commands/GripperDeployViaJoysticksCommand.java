package competition.subsystems.gripperdeploy.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import xbot.common.command.BaseCommand;

public class GripperDeployJoysticksCommand extends BaseCommand {
    
    final GripperDeploySubsystem gripperDeploySubsystem;
    final OperatorInterface oi;
    
    @Inject
    public GripperDeployJoysticksCommand (GripperDeploySubsystem gripperDeploySubsystem, OperatorInterface oi) {
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
        double power = oi.operatorGamepad.getLeftVector().y;
        gripperDeploySubsystem.motor.simpleSet(power);
    }
}
