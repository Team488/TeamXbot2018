package competition.subsystems.wrist.commands;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristDangerousOverrideCommand extends BaseCommand {
    WristSubsystem wrist;
    OperatorInterface oi;
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        wrist.insanelyDangerousSetPower(oi.driverGamepad.getLeftVector().y);
    }
    
}
