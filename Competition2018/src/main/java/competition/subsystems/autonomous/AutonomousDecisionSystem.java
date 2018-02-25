package competition.subsystems.autonomous;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;

@Singleton
public class AutonomousDecisionSystem extends BaseSubsystem {

    GameDataSource gameData;
    
    @Inject
    public AutonomousDecisionSystem(GameDataSource gameData) {
        this.gameData = gameData;
    }
    
    public Supplier<List<FieldPose>> getAutoPathToFeature(GameFeature feature) {
        return () -> chooseBestPathToFeature(feature);
    }
    
    private List<FieldPose> chooseBestPathToFeature(GameFeature feature) {
        OwnedSide targetSide = gameData.getOwnedSide(feature);
        
        switch (feature) {
        case SWITCH_NEAR:
            switch (targetSide) {
            case LEFT:
                log.info("Creating path to Left Switch");
                return createPathToLeftSwitch();
            case RIGHT:
                log.info("Creating path to Right Switch");
                return createPathToRightSwitch();
            case UNKNOWN:
                log.warn("Jaci's library could not parse which Switch side to visit in auto. Going nowhere");
                return createPathToNowhere();
            default: 
                log.warn("Somehow no idea where to go. Going nowhere.");
                return createPathToNowhere();
            }
        case SCALE:
            switch (targetSide) {
            case LEFT:
                log.info("Creating path to Left Scale");
                return createPathToLeftScale();
            case RIGHT:
                log.info("Creating path to Right Scale");
                return createPathToRightScale();
            case UNKNOWN:
                log.warn("Jaci's library could not parse which Scale side to visit in auto. Going nowhere");
                return createPathToNowhere();
            default: 
                log.warn("Somehow no idea where to go. Going nowhere.");
                return createPathToNowhere();
            }
            default: 
                log.warn("Somehow no idea where to go. Going nowhere.");
                return createPathToNowhere();
        }
        
        
    }
    
    public List<FieldPose> createPathToRightScale() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0*12, 11*12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-2*12, 22*12), new ContiguousHeading(120)));
        return points;
    }
    
    public List<FieldPose> createPathToLeftScale() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0*12, 18*12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-16*12, 18*12), new ContiguousHeading(180)));
        points.add(new FieldPose(new XYPair(-16*12, 18*12), new ContiguousHeading(180)));
        points.add(new FieldPose(new XYPair(-16*12, 22*12), new ContiguousHeading(90)));
        return points;
    }
    
    public List<FieldPose> createPathToRightSwitch() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        /*points.add(new FieldPose(new XYPair(-1.7*12, 3*12), new ContiguousHeading(135)));
        points.add(new FieldPose(new XYPair(-3.4*12, 5.3*12), new ContiguousHeading(135)));*/
        points.add(new FieldPose(new XYPair(-6*12, 10*12), new ContiguousHeading(90)));
        
        return points;
    }
    
    public List<FieldPose> createPathToLeftSwitch() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        
        points.add(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        //points.add(new FieldPose(new XYPair(-1.35*12, 2.5*12), new ContiguousHeading(127.5)));
        //points.add(new FieldPose(new XYPair(-5.35*12, 3*12), new ContiguousHeading(160)));
        points.add(new FieldPose(new XYPair(-7.35*12, 4*12), new ContiguousHeading(180)));
        //points.add(new FieldPose(new XYPair(-10.35*12, 4*12), new ContiguousHeading(165)));
        //points.add(new FieldPose(new XYPair(-12.35*12, 5*12), new ContiguousHeading(150)));
        //points.add(new FieldPose(new XYPair(-13.85*12, 7*12), new ContiguousHeading(120)));
        points.add(new FieldPose(new XYPair(-16*12, 10*12), new ContiguousHeading(90)));

        return points;
    }
    
    private List<FieldPose> createPathToNowhere() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0, 0), new ContiguousHeading(90)));
        return points;
    }
}
