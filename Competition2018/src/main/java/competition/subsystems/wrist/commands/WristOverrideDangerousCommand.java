package competition.subsystems.wrist.commands;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristOverrideDangerousCommand extends BaseCommand {
    WristSubsystem wrist;
    OperatorInterface oi;
    
    @Override
    public void initialize() {
        wrist.insanelyDangerousSetPower(oi.driverGamepad.getLeftVector().y);
    }

    @Override
    public void execute() {
        
    }
    
}
