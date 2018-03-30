package competition.subsystems.autonomous;

import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.commandgroups.CrossAutoLineCommandGroup;
import competition.commandgroups.DynamicScoreOnScaleCommandGroup;
import competition.commandgroups.DynamicScoreOnSwitchCommandGroup;
import competition.commandgroups.MultiCubeNearScaleCommandGroup;
import edu.wpi.first.wpilibj.command.Command;
import openrio.powerup.MatchData.GameFeature;
import xbot.common.command.BaseSubsystem;

@Singleton
public class AutonomousCommandSupplier extends BaseSubsystem {

    final AutonomousPathSupplier pathSupplier;
    final GameDataSource gameData;
    
    MultiCubeNearScaleCommandGroup multiCubeNearScale;
    DynamicScoreOnScaleCommandGroup singleCubeAnyScale;
    DynamicScoreOnSwitchCommandGroup singleCubeSwitch;
    CrossAutoLineCommandGroup crossLine;
    
    GameFeature goalFeature;
    
    @Inject
    public AutonomousCommandSupplier(
            AutonomousPathSupplier pathSupplier, 
            GameDataSource gameData,
            MultiCubeNearScaleCommandGroup multiCubeNearScale,
            DynamicScoreOnScaleCommandGroup singleCubeAnyScale,
            DynamicScoreOnSwitchCommandGroup singleCubeSwitch,
            CrossAutoLineCommandGroup crossLine) {
        this.pathSupplier = pathSupplier;
        this.gameData = gameData;
        
        this.multiCubeNearScale = multiCubeNearScale;
        this.singleCubeAnyScale = singleCubeAnyScale;
        this.singleCubeSwitch = singleCubeSwitch;
        this.crossLine = crossLine;
    }
    
    public void setGoalElement(GameFeature feature) {
        goalFeature = feature;
    }
    
    public Supplier<Command> getBestAuto() {
        return () -> getBestAutoForGoal();
    }
    
    public Command getBestAutoForGoal() {
        switch (goalFeature) {
        case SWITCH_NEAR:
            return singleCubeSwitch;
        case SCALE:
            // if we have a matching side, try the multi cube auto
            if (pathSupplier.matchingSide()) {
                return multiCubeNearScale;
            }
            return singleCubeAnyScale;
        default:
            return crossLine;
        }
    }
}
