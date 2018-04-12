package competition.subsystems.fhds.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.fhds.FlyingHookDeliverySubsystem;
import xbot.common.command.BaseCommand;

public class DroneOperatorControlledCommand extends BaseCommand {

    private final FlyingHookDeliverySubsystem droneSubsystem;
    private final OperatorInterface oi;
    
    @Inject
    public DroneOperatorControlledCommand(FlyingHookDeliverySubsystem droneSubsystem, OperatorInterface oi) {
        this.droneSubsystem = droneSubsystem;
        this.oi = oi;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        droneSubsystem.handleOperatorInput(oi.operatorGamepad.getLeftVector(), oi.operatorGamepad.getRightVector());
    }

}
