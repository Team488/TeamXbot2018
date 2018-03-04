package competition.subsystems.climb.commands;

import com.google.inject.Inject;
import competition.subsystems.climb.ClimbSubsystem;
import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.zed_deploy.ZedDeploySubsystem;
import xbot.common.command.BaseCommand;

public class DecendClimberCommand extends BaseCommand {

    ClimbSubsystem climb;
    ClimberDeploySubsystem deploy;
    //ZedDeploySubsystem zedDeploy;

    @Inject
    public DecendClimberCommand(ClimbSubsystem climb, ClimberDeploySubsystem deploy) {//, ZedDeploySubsystem zedDeploy) {
        this.climb = climb;
        this.deploy = deploy;
        //this.zedDeploy = zedDeploy;
        this.requires(climb);
        //this.requires(zedDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        //zedDeploy.setIsExtended(false);
    }

    @Override
    public void execute() {
        // If the pay out is too fast compared to the deploy arm, stop for a moment to let the deploy arm catch up.
        if (climb.percentPayedOut() < deploy.percentExtended() + .2) {
            climb.descend();
        } else {
            climb.stop();
        }
    }

}
