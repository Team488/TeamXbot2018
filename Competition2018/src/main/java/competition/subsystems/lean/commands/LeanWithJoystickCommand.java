package competition.subsystems.lean.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.lean.*;
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
        // TODO Auto-generated method stub
        
    }
    @Override
    public void execute() {
        // TODO Auto-generated method stub
        leanSubsystem.setLeanSpeed(oi.operatorGamepad.getLeftVector().x);
    }
}
