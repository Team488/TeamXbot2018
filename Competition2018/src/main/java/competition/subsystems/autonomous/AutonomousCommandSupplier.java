package competition.subsystems.autonomous;

import java.sql.Savepoint;
import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.commandgroups.CrossAutoLineCommandGroup;
import competition.commandgroups.ScoreOnScaleCommandGroup;
import competition.commandgroups.ScoreOnSwitchCommandGroup;
import competition.commandgroups.MultiCubeNearScaleCommandGroup;
import edu.wpi.first.wpilibj.command.Command;
import openrio.powerup.MatchData.GameFeature;
import xbot.common.command.BaseSubsystem;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class AutonomousCommandSupplier extends BaseSubsystem {
    
    public enum AutonomousMetaprogram {
        Switch,
        Scale,
        Opportunistic,
        CrossLine
    }

    final AutonomousPathSupplier pathSupplier;
    final GameDataSource gameData;
    
    MultiCubeNearScaleCommandGroup multiCubeNearScale;
    ScoreOnScaleCommandGroup singleCubeAnyScale;
    ScoreOnSwitchCommandGroup singleCubeSwitch;
    CrossAutoLineCommandGroup crossLine;
    
    AutonomousMetaprogram metaprogram;
    
    private final StringProperty goalFeatureProp;
    
    @Inject
    public AutonomousCommandSupplier(
            AutonomousPathSupplier pathSupplier, 
            GameDataSource gameData,
            MultiCubeNearScaleCommandGroup multiCubeNearScale,
            ScoreOnScaleCommandGroup singleCubeAnyScale,
            ScoreOnSwitchCommandGroup singleCubeSwitch,
            CrossAutoLineCommandGroup crossLine,
            XPropertyManager propMan) {
        this.pathSupplier = pathSupplier;
        this.gameData = gameData;
        this.multiCubeNearScale = multiCubeNearScale;
        this.singleCubeAnyScale = singleCubeAnyScale;
        this.singleCubeSwitch = singleCubeSwitch;
        this.crossLine = crossLine;
        
        goalFeatureProp = propMan.createEphemeralProperty(getPrefix() + "Goal Feature", "Not set yet");
        
        log.info("Created.");
    }
    
    public void setGoalFeature(AutonomousMetaprogram metaprogram) {
        log.info("Metaprogram is" + metaprogram.toString());
        this.metaprogram = metaprogram;
        goalFeatureProp.set(metaprogram.toString());
    }
    
    public Supplier<Command> getBestAuto() {
        return () -> chooseAutoToServeMetaprogram();
    }
    
    public Command chooseAutoToServeMetaprogram() {
        switch (metaprogram) {
        case Switch:
            log.info("Choosing: Doing single-cube switch");
            return singleCubeSwitch;
        case Scale:
            // if we have a matching side, try the multi cube auto
            log.info("Choosing: Doing single cube scale");
            return singleCubeAnyScale;
        default:
            log.info("Choosing: Crossing auto line");
            return crossLine;
        }
    }
}
