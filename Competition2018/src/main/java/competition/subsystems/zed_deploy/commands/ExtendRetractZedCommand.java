package competition.subsystems.zed_deploy.commands;

import com.google.inject.Inject;

import competition.subsystems.zed_deploy.ZedDeploySubsystem;
import xbot.common.command.BaseCommand;

public class ExtendRetractZedCommand extends BaseCommand {
    ZedDeploySubsystem deploySubsystem;
    private boolean isExtended;
    
    @Inject
    public ExtendRetractZedCommand(ZedDeploySubsystem deploySubsystem) {
        this.requires(deploySubsystem);
        this.deploySubsystem = deploySubsystem;
    }
    
    public void setIsExtended(boolean isExtended) {
        this.isExtended = isExtended;
    }

    @Override
    public void initialize() {
        log.info((isExtended ? "Extending" : "Retracting") + " ZED");
        deploySubsystem.setIsExtended(isExtended);
    }

    @Override
    public void execute() {
        // Intentionally left blank
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
