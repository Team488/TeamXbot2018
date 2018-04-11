package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.commandgroups.ScoreOnSwitchCommandGroup;
import xbot.common.command.BaseCommand;

public class SelectDynamicScoreOnSwitchCommand extends BaseCommand {

    ScoreOnSwitchCommandGroup dynamicScoreSwitch;
    AutonomousCommandSelector autoSelector;
    
    @Inject
    public SelectDynamicScoreOnSwitchCommand(AutonomousCommandSelector autoSelector, ScoreOnSwitchCommandGroup dynamicScoreSwitch) {
        this.setRunWhenDisabled(true);
        this.autoSelector = autoSelector;
        this.dynamicScoreSwitch = dynamicScoreSwitch;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        autoSelector.setCurrentAutonomousCommand(dynamicScoreSwitch);
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
