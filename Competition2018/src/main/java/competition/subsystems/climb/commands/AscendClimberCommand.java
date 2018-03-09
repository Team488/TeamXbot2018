package competition.subsystems.climb.commands;

import com.google.inject.Inject;
import competition.subsystems.climb.ClimbSubsystem;
import xbot.common.command.BaseCommand;

public class AscendClimberCommand extends BaseCommand {

    ClimbSubsystem climb;
   // ZedDeploySubsystem zedDeploy;

    @Inject
    public AscendClimberCommand(ClimbSubsystem climb) {//, ZedDeploySubsystem zedDeploy) {
        this.climb = climb;
        //this.zedDeploy = zedDeploy;
        this.requires(climb);
       // this.requires(zedDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
       // zedDeploy.setIsExtended(false);
    }

    @Override
    public void execute() {
        climb.ascend();
    }

}
