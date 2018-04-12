package competition.subsystems.fhds.commands;

import com.google.inject.Inject;

import competition.subsystems.fhds.FlyingHookDeliverySubsystem;
import xbot.common.command.BaseCommand;

public class DroneEnableCommand extends BaseCommand {

    private final FlyingHookDeliverySubsystem droneSubsystem;
    
    @Inject
    public DroneEnableCommand(FlyingHookDeliverySubsystem droneSubsystem) {
        this.droneSubsystem = droneSubsystem;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        
        if (droneSubsystem.getDroneThread().isAlive()) {
            log.error("There is a drone thread already active");
        } else {
            droneSubsystem.getDroneThread().start();
            log.info("It's(Drone) alive!!");
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
