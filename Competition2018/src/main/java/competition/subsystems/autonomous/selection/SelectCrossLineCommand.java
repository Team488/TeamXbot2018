package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.commandgroups.CrossAutoLineCommandGroup;
import xbot.common.command.BaseCommand;

public class SelectCrossLineCommand extends BaseCommand {

    CrossAutoLineCommandGroup crossLine;
    AutonomousCommandSelector autoSelector;
    
    @Inject
    public SelectCrossLineCommand(
            AutonomousCommandSelector autoSelector, 
            CrossAutoLineCommandGroup crossLine) {
        this.setRunWhenDisabled(true);
        this.autoSelector = autoSelector;
        this.crossLine = crossLine;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        autoSelector.setCurrentAutonomousCommand(crossLine);
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
