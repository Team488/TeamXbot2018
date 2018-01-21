package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import xbot.common.command.BaseCommand;

public class RetractClimberArmCommand extends BaseCommand {

    ClimberDeploySubsystem deploy;

    @Inject
    public RetractClimberArmCommand (ClimberDeploySubsystem deploy) {
        this.deploy = deploy;
        this.requires(deploy);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        deploy.retractClimberArm();
    }

    @Override
    public boolean isFinished() {
        return deploy.hitBarHeight();
    }
    public void end() {
        deploy.stopClimberArm();
    }
}
