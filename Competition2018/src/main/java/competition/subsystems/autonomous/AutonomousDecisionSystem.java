package competition.subsystems.autonomous;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class AutonomousDecisionSystem extends BaseSubsystem {

    final GameDataSource gameData;
    final BooleanProperty robotOnRight;
    final DoubleProperty autonomousDelay;
    
    @Inject
    public AutonomousDecisionSystem(GameDataSource gameData, XPropertyManager propMan) {
        this.gameData = gameData;
        robotOnRight = propMan.createPersistentProperty(getPrefix() + "Robot On Right", true);
        autonomousDelay = propMan.createPersistentProperty(getPrefix() + "Seconds to Delay Auto", 0.0);
        
        constrainDelay();
    }
    
    public void setRobotPosition(boolean onRight) {
        robotOnRight.set(onRight);
    }
    
    public void changeAutoDelay(double amount) {
        autonomousDelay.set(autonomousDelay.get() + amount);
        constrainDelay();
    }
    
    private void constrainDelay() {
        autonomousDelay.set(MathUtils.constrainDouble(autonomousDelay.get(), 0, 15));
    }
    
    public Supplier<List<FieldPose>> getAutoPathToFeature(GameFeature feature) {
        return () -> chooseBestPathToFeature(feature, robotOnRight.get());
    }
    
    private List<FieldPose> mirrorPath(List<FieldPose> path) {
        List<FieldPose> flippedPath = new ArrayList<FieldPose>();
        
        for (FieldPose pose : path) {
            flippedPath.add(new FieldPose(
                    new XYPair(-pose.getPoint().x, pose.getPoint().y),
                    new ContiguousHeading(pose.getHeading().getValue())
                )
            );
        }
        
        return flippedPath;
    }
    
    private List<FieldPose> chooseBestPathToFeature(GameFeature feature, boolean onRight) {
        OwnedSide targetSide = gameData.getOwnedSide(feature);
        log.info("Target Side is: " + targetSide);
        List<FieldPose> bestPath = createPathToNowhere();
        switch (feature) {
        case SWITCH_NEAR:
            switch (targetSide) {
            case LEFT:
                log.info("Creating path to Left Switch Plate");
                if (onRight) {
                    bestPath = createPathToDistantSwitchPlate();
                } else {
                    bestPath = mirrorPath(createPathToNearbySwitchPlate());
                }                
                break;
            case RIGHT:
                log.info("Creating path to Right Switch Plate");
                if (onRight) {
                    bestPath = createPathToNearbySwitchPlate();
                } else {
                    bestPath = mirrorPath(createPathToDistantSwitchPlate());
                }  
                break;
            case UNKNOWN:
                log.warn("Jaci's library could not parse which Switch side to visit in auto. Going nowhere");
                bestPath = createPathToNowhere();
                break;
            default: 
                log.warn("Somehow no idea where to go. Going nowhere.");
                bestPath = createPathToNowhere();
                break;
            }
            break;
        case SCALE:
            switch (targetSide) {
            case LEFT:
                log.info("Creating path to Left Scale");
                if (onRight) {
                    bestPath = createPathToDistantScalePlate();
                } else {
                    bestPath = mirrorPath(createPathToNearbyScalePlate());
                }    
                break;
            case RIGHT:
                log.info("Creating path to Right Scale");
                if (onRight) {
                    bestPath = createPathToNearbyScalePlate();
                } else {
                    bestPath = mirrorPath(createPathToDistantScalePlate());
                }    
                break;
            case UNKNOWN:
                log.warn("Jaci's library could not parse which Scale side to visit in auto. Going nowhere");
                bestPath = createPathToNowhere();
                break;
            default: 
                log.warn("Somehow no idea where to go. Going nowhere.");
                bestPath = createPathToNowhere();
                break;
            }
            break;
            default: 
                log.warn("Somehow no idea where to go. Going nowhere.");
                bestPath = createPathToNowhere();
                break;
        }
        
        return bestPath;
    }
    
    public List<FieldPose> createPathToNearbyScalePlate() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0*12, 11*12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-1*12, 20*12), new ContiguousHeading(120)));
        return points;
    }
    
    public List<FieldPose> createPathToDistantScalePlate() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0*12, 14*12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-16*12, 17*12), new ContiguousHeading(180)));
        points.add(new FieldPose(new XYPair(-16*12, 17*12), new ContiguousHeading(180)));
        points.add(new FieldPose(new XYPair(-19*12, 20.5*12), new ContiguousHeading(70)));
        return points;
    }
    
    public List<FieldPose> createPathToNearbySwitchPlate() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        /*points.add(new FieldPose(new XYPair(-1.7*12, 3*12), new ContiguousHeading(135)));
        points.add(new FieldPose(new XYPair(-3.4*12, 5.3*12), new ContiguousHeading(135)));*/
        points.add(new FieldPose(new XYPair(-6*12, 7.5*12), new ContiguousHeading(90)));
        
        return points;
    }
    
    public List<FieldPose> createPathToDistantSwitchPlate() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        
        points.add(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        //points.add(new FieldPose(new XYPair(-1.35*12, 2.5*12), new ContiguousHeading(127.5)));
        //points.add(new FieldPose(new XYPair(-5.35*12, 3*12), new ContiguousHeading(160)));
        points.add(new FieldPose(new XYPair(-7.35*12, 2*12), new ContiguousHeading(180)));
        //points.add(new FieldPose(new XYPair(-10.35*12, 4*12), new ContiguousHeading(165)));
        //points.add(new FieldPose(new XYPair(-12.35*12, 5*12), new ContiguousHeading(150)));
        //points.add(new FieldPose(new XYPair(-13.85*12, 7*12), new ContiguousHeading(120)));
        points.add(new FieldPose(new XYPair(-15.6*12, 7.5*12), new ContiguousHeading(90)));

        return points;
    }
    
    private List<FieldPose> createPathToNowhere() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0, 0), new ContiguousHeading(90)));
        return points;
    }
}
