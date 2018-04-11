package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.commandgroups.ScoreOnScaleCommandGroup;
import xbot.common.command.BaseCommand;

public class SelectDynamicScoreOnScaleCommand extends BaseCommand {

    ScoreOnScaleCommandGroup dynamicScoreScale;
    AutonomousCommandSelector autoSelector;
    
    @Inject
    public SelectDynamicScoreOnScaleCommand(AutonomousCommandSelector autoSelector, ScoreOnScaleCommandGroup dynamicScoreScale) {
        this.setRunWhenDisabled(true);
        this.autoSelector = autoSelector;
        this.dynamicScoreScale = dynamicScoreScale;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        autoSelector.setCurrentAutonomousCommand(dynamicScoreScale);
    }

    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
