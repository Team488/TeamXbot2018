package competition.subsystems.fhds.commands;

import com.google.inject.Inject;

import competition.subsystems.fhds.FlyingHookDeliverySubsystem;
import xbot.common.command.BaseCommand;

public class DroneDisableCommand extends BaseCommand {

    private final FlyingHookDeliverySubsystem droneSubsystem;
    
    @Inject
    public DroneDisableCommand(FlyingHookDeliverySubsystem droneSubsystem) {
        this.droneSubsystem = droneSubsystem;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        
        if (droneSubsystem.getDroneThread().isAlive()) {
            log.error("There is no drone thread to interrupt");
        } else {
            droneSubsystem.getDroneThread().interrupt();
            log.info("The drone thread has been interrupted");
        }
    }

    @Override
    public void execute() {
        // Do Nothing
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
