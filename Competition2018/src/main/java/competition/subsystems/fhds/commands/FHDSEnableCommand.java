package competition.subsystems.fhds.commands;

import com.google.inject.Inject;

import competition.subsystems.fhds.FlyingHookDeliverySensorSubsystem;
import xbot.common.command.BaseCommand;

public class FHDSEnableCommand extends BaseCommand {

    private final FlyingHookDeliverySensorSubsystem fhdsSubsystem;
    
    @Inject
    public FHDSEnableCommand(FlyingHookDeliverySensorSubsystem fhdsSubsystem) {
        this.fhdsSubsystem = fhdsSubsystem;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        fhdsSubsystem.startFHDSControl();
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
