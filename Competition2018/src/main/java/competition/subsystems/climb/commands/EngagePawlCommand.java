package competition.subsystems.climb.commands;

import com.google.inject.Inject;

import competition.subsystems.climb.ClimbSubsystem;
import xbot.common.command.BaseCommand;

public class EngagePawlCommand extends BaseCommand{
    
    ClimbSubsystem climb;
    
    @Inject
    public EngagePawlCommand(ClimbSubsystem climb) {
        this.climb = climb;
        this.requires(climb);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    @Override
    public void execute() {
        climb.engagePawl();
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }

}
