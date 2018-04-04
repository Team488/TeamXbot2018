package competition.subsystems.lights.commands;

import com.google.inject.Inject;

import competition.subsystems.lights.LightsSubsystem;
import xbot.common.command.BaseCommand;

public class SetLightStateCommand extends BaseCommand {

    private boolean value;
    final LightsSubsystem lights;
    
    @Inject
    public SetLightStateCommand(LightsSubsystem lights) {
        this.lights = lights;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing with value: " + value);
        lights.setOn(value);
    }
    
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void execute() {
    }
}
