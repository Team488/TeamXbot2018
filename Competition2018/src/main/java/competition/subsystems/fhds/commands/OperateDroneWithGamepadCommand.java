package competition.subsystems.fhds.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.fhds.FlyingHookDeliverySensorSubsystem;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class OperateDroneWithGamepadCommand extends BaseCommand {

    private final FlyingHookDeliverySensorSubsystem drone;
    private final OperatorInterface oi;
    
    @Inject
    public OperateDroneWithGamepadCommand(FlyingHookDeliverySensorSubsystem drone, WristSubsystem wrist, ElevatorSubsystem elevator, OperatorInterface oi) {
        this.drone = drone;
        this.oi = oi;
        
        this.requires(drone);
        
        // Require other systems that might be using the operator gamepad
        this.requires(wrist);
        this.requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        drone.handleOperatorInput(oi.operatorGamepad.getLeftVector(), oi.operatorGamepad.getRightVector());
    }
}
