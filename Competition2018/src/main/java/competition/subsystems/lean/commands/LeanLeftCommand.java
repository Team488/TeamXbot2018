package competition.subsystems.lean.commands;

import com.google.inject.Inject;

import competition.subsystems.lean.LeanSubsystem;
import xbot.common.command.BaseCommand;

public class LeanLeftCommand extends BaseCommand {

    LeanSubsystem leaner;
    
    @Inject
    public LeanLeftCommand(LeanSubsystem leaner) {
        this.leaner = leaner;
        this.requires(leaner); 
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        leaner.leanLeft();
    }
    
    @Override
    public boolean isFinished() {
        return leaner.hitBar();
    }
    public void end() {
        leaner.stopLean();
    }

}

