package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import xbot.common.command.BaseCommand;

public class DecreaseSpeedCommand extends BaseCommand{

    ClimberDeploySubsystem deploy;

    @Inject 
    public DecreaseSpeedCommand(ClimberDeploySubsystem deploy) {
        this.deploy = deploy;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        deploy.decreaseSpeed();
    }

    @Override
    public void execute() {
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
