package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousCommandSupplier;
import openrio.powerup.MatchData.GameFeature;
import xbot.common.command.BaseCommand;

public class SelectAdvancedAutonomous extends BaseCommand {

    AutonomousCommandSupplier autoCommandSupplier;
    AutonomousCommandSelector autoSelector;
    GameFeature goalFeature;
    
    @Inject
    public SelectAdvancedAutonomous(AutonomousCommandSelector autoSelector, AutonomousCommandSupplier autoCommandSupplier) {
        this.autoSelector = autoSelector;
        this.autoCommandSupplier = autoCommandSupplier;
        this.setRunWhenDisabled(true);
    }
    
    public void setGoalFeature(GameFeature feature) {
        this.goalFeature = feature;
    }

    @Override
    public void initialize() {
        autoCommandSupplier.setGoalFeature(goalFeature);
        autoSelector.setCurrentAutonomousCommandSupplier(autoCommandSupplier.getBestAuto());
        
    }

    @Override
    public void execute() {
    }    
}
