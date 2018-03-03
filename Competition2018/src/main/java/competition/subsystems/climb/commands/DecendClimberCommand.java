package competition.subsystems.climb.commands;

import com.google.inject.Inject;
import competition.subsystems.climb.ClimbSubsystem;
import competition.subsystems.zed_deploy.ZedDeploySubsystem;
import xbot.common.command.BaseCommand;

public class DecendClimberCommand extends BaseCommand {

    ClimbSubsystem climb;
    ZedDeploySubsystem zedDeploy;

    @Inject
    public DecendClimberCommand(ClimbSubsystem climb, ZedDeploySubsystem zedDeploy) {
        this.climb = climb;
        this.zedDeploy = zedDeploy;
        this.requires(climb);
        this.requires(zedDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        zedDeploy.setIsExtended(false);
    }

    @Override
    public void execute() {
        climb.decend();
    }

}
