package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ControlElevatorViaJoystickCommand extends BaseCommand {

    OperatorInterface oi;
    ElevatorSubsystem elevator;
    DoubleProperty hat;
    
    @Inject
    public ControlElevatorViaJoystickCommand(ElevatorSubsystem elevator, OperatorInterface oi, XPropertyManager propMan) {
        this.elevator = elevator;
        this.oi = oi;
        this.requires(elevator);
        
        hat = propMan.createEphemeralProperty(getPrefix() + "Hat", 0);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        elevator.setPower(oi.operatorGamepad.getRightStickY());
        
        hat.set(oi.operatorGamepad.getPOV());
    }

}
