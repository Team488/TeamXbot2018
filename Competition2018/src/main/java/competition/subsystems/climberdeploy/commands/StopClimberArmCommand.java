package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import xbot.common.command.BaseCommand;

public class StopClimberArmCommand extends BaseCommand{

    ClimberDeploySubsystem deploy;

    @Inject
    public StopClimberArmCommand(ClimberDeploySubsystem deploy) {
        this.deploy = deploy;
        this.requires(deploy); 

    }
    @Override
    public void initialize() {
        log.info("Initializing");        
    }

    @Override
    public void execute() {
        deploy.stopClimberArm();
    }

}
