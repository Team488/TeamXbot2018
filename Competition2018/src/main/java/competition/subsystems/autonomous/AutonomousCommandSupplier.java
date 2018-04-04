package competition.subsystems.autonomous;

import java.sql.Savepoint;
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
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class AutonomousCommandSupplier extends BaseSubsystem {

    final AutonomousPathSupplier pathSupplier;
    final GameDataSource gameData;
    
    MultiCubeNearScaleCommandGroup multiCubeNearScale;
    DynamicScoreOnScaleCommandGroup singleCubeAnyScale;
    DynamicScoreOnSwitchCommandGroup singleCubeSwitch;
    CrossAutoLineCommandGroup crossLine;
    
    GameFeature goalFeature;
    
    private final StringProperty goalFeatureProp;
    
    @Inject
    public AutonomousCommandSupplier(
            AutonomousPathSupplier pathSupplier, 
            GameDataSource gameData,
            MultiCubeNearScaleCommandGroup multiCubeNearScale,
            DynamicScoreOnScaleCommandGroup singleCubeAnyScale,
            DynamicScoreOnSwitchCommandGroup singleCubeSwitch,
            CrossAutoLineCommandGroup crossLine,
            XPropertyManager propMan) {
        log.info("Creating.");
        this.pathSupplier = pathSupplier;
        this.gameData = gameData;
        log.info("Creating, phase two");
        this.multiCubeNearScale = multiCubeNearScale;
        this.singleCubeAnyScale = singleCubeAnyScale;
        log.info("Creating, phase three");
        this.singleCubeSwitch = singleCubeSwitch;
        this.crossLine = crossLine;
        log.info("Created.");
        
        goalFeatureProp = propMan.createEphemeralProperty(getPrefix() + "Goal Feature", "Not set yet");
    }
    
    public void setGoalFeature(GameFeature feature) {
        log.info("Goal feature now saved as " + feature.toString());
        goalFeature = feature;
        goalFeatureProp.set(goalFeature.toString());
    }
    
    public Supplier<Command> getBestAuto() {
        return () -> getBestAutoForGoal();
    }
    
    public Command getBestAutoForGoal() {
        switch (goalFeature) {
        case SWITCH_NEAR:
            log.info("Choosing: Doing single-cube switch");
            return singleCubeSwitch;
        case SCALE:
            // if we have a matching side, try the multi cube auto
            if (pathSupplier.matchingSide()) {
                log.info("Choosing: Sides match, doing multicube scale");
                return multiCubeNearScale;
            }
            log.info("Choosing: Sides don't match, doing single cube scale");
            return singleCubeAnyScale;
        default:
            log.info("Choosing: Crossing auto line");
            return crossLine;
        }
    }
}
