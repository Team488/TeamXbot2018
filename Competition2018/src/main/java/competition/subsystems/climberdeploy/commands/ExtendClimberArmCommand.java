package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import xbot.common.command.BaseCommand;

public class ExtendClimberArmCommand extends BaseCommand {

    ClimberDeploySubsystem deploy;
    //ZedDeploySubsystem zedDeploy;

    @Inject
    public ExtendClimberArmCommand(ClimberDeploySubsystem deploy) {//, ZedDeploySubsystem zedDeploy) {
        this.deploy = deploy;
        //this.zedDeploy = zedDeploy;
        this.requires(deploy);
        //this.requires(zedDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
       //zedDeploy.setIsExtended(false);
    }

    @Override
    public void execute() {
        deploy.extendClimberArm();
    }

    @Override
    public boolean isFinished() {
        return deploy.hitBarHeight();
    }

    public void end() {
        deploy.stopClimberArm();
    }
}
