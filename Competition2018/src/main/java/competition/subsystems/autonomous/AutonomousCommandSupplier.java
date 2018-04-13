package competition.subsystems.autonomous;

import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.commandgroups.CrossAutoLineCommandGroup;
import competition.commandgroups.MultiCubeNearScaleCommandGroup;
import competition.commandgroups.ScoreOnScaleCommandGroup;
import competition.commandgroups.ScoreOnSwitchCommandGroup;
import competition.subsystems.autonomous.commands.DriveNowhereCommand;
import edu.wpi.first.wpilibj.command.Command;
import xbot.common.command.BaseSubsystem;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class AutonomousCommandSupplier extends BaseSubsystem {
    
    public enum AutonomousMetaprogram {
        DoNothing,
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
    DriveNowhereCommand driveNowhere;
    
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
            DriveNowhereCommand driveNowhere,
            XPropertyManager propMan) {
        this.pathSupplier = pathSupplier;
        this.gameData = gameData;
        this.multiCubeNearScale = multiCubeNearScale;
        this.singleCubeAnyScale = singleCubeAnyScale;
        this.singleCubeSwitch = singleCubeSwitch;
        this.crossLine = crossLine;
        this.driveNowhere = driveNowhere;
        
        goalFeatureProp = propMan.createEphemeralProperty(getPrefix() + "Goal Feature", "Not set yet");
        this.metaprogram = AutonomousMetaprogram.DoNothing;
        log.info("Created.");
    }
    
    public void setMetaprogram(AutonomousMetaprogram metaprogram) {
        log.info("Metaprogram is " + metaprogram.toString());
        this.metaprogram = metaprogram;
        goalFeatureProp.set(metaprogram.toString());
    }
    
    public Supplier<Command> getAutoSupplier() {
        return () -> chooseAutoToServeMetaprogram();
    }
    
    public Command chooseAutoToServeMetaprogram() {
        log.info("Choosing metaprogram; setting is " + metaprogram);
        switch (metaprogram) {
        case Switch:
            log.info("Choosing: Doing single-cube switch");
            return singleCubeSwitch;
        case Scale:
            // if we have a matching side, try the multi cube auto
            log.info("Choosing: Doing single cube scale");
            return singleCubeAnyScale;
        case CrossLine:
            log.info("Choosing: Crossing auto line");
            return crossLine;
        case DoNothing:
        default:
            log.info("Choosing: Do nothing");
            return driveNowhere;
        }
    }
}
