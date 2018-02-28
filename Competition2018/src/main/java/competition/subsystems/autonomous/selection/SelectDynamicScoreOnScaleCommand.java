package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.commandgroups.DynamicScoreOnScaleCommandGroup;
import xbot.common.command.BaseCommand;

public class SelectDynamicScoreOnScaleCommand extends BaseCommand {

    DynamicScoreOnScaleCommandGroup dynamicScoreScale;
    AutonomousCommandSelector autoSelector;
    
    @Inject
    public SelectDynamicScoreOnScaleCommand(AutonomousCommandSelector autoSelector, DynamicScoreOnScaleCommandGroup dynamicScoreScale) {
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
