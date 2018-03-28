package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.commands.DriveNowhereCommand;
import xbot.common.command.BaseCommand;

public class SelectDoNothingCommand extends BaseCommand {

    DriveNowhereCommand goNowhere;
    AutonomousCommandSelector autoSelector;
    
    @Inject
    public SelectDoNothingCommand(
            AutonomousCommandSelector autoSelector, 
            DriveNowhereCommand goNowhere) {
        this.setRunWhenDisabled(true);
        this.autoSelector = autoSelector;
        this.goNowhere = goNowhere;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        autoSelector.setCurrentAutonomousCommand(goNowhere);
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
