package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class ControlElevatorViaJoystickCommand extends BaseCommand {

    OperatorInterface oi;
    ElevatorSubsystem elevator;
    
    @Inject
    public ControlElevatorViaJoystickCommand(ElevatorSubsystem elevator, OperatorInterface oi) {
        this.elevator = elevator;
        this.oi = oi;
        this.requires(elevator);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        elevator.setPower(oi.operatorGamepad.getRightStickY());
    }

}
