package competition.subsystems.fhds.commands;

import com.google.inject.Inject;

import competition.subsystems.fhds.FlyingHookDeliverySensorSubsystem;
import xbot.common.command.BaseCommand;

public class DroneEnableCommand extends BaseCommand {

    private final FlyingHookDeliverySensorSubsystem droneSubsystem;
    
    @Inject
    public DroneEnableCommand(FlyingHookDeliverySensorSubsystem droneSubsystem) {
        this.droneSubsystem = droneSubsystem;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        droneSubsystem.startDroneControl();
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
