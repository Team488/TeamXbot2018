package competition.subsystems.elevator.commands;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class ElevatorDangerousOverrideCommand extends BaseCommand {
    
    ElevatorSubsystem elevator;
    OperatorInterface oi;

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        elevator.insanelyDangerousSetPower(oi.driverGamepad.getRightStickY());
    }

}
