package competition.subsystems.autonomous;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.commands.TotalRobotPoint;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;
import xbot.common.command.BaseSubsystem;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.drive.RabbitPoint.PointDriveStyle;
import xbot.common.subsystems.drive.RabbitPoint.PointTerminatingType;
import xbot.common.subsystems.drive.RabbitPoint.PointType;

@Singleton
public class AutonomousPathSupplier extends BaseSubsystem {

    final GameDataSource gameData;
    final StringProperty robotLocation;
    final DoubleProperty autonomousDelay;

    private StartingLocations startingLocation;

    public enum StartingLocations {
        Left, Middle, Right
    }

    @Inject
    public AutonomousPathSupplier(GameDataSource gameData, XPropertyManager propMan) {
        this.gameData = gameData;
        startingLocation = StartingLocations.Right;
        robotLocation = propMan.createPersistentProperty(getPrefix() + "Robot Location", "Not Set");
        autonomousDelay = propMan.createPersistentProperty(getPrefix() + "Seconds to Delay Auto", 0.0);
        constrainDelay();
    }

    public void setRobotPosition(StartingLocations startingLocation) {
        this.startingLocation = startingLocation;
        robotLocation.set(startingLocation.toString());
    }

    public void changeAutoDelay(double amount) {
        autonomousDelay.set(autonomousDelay.get() + amount);
        constrainDelay();
    }

    private void constrainDelay() {
        autonomousDelay.set(MathUtils.constrainDouble(autonomousDelay.get(), 0, 15));
    }

    public double getDelay() {
        return autonomousDelay.get();
    }

    public Supplier<List<RabbitPoint>> getAutoPathToFeature(GameFeature feature) {
        return () -> chooseBestPathToFeature(feature, startingLocation);
    }
    
    public Supplier<List<TotalRobotPoint>> getAdvancedAutoPathToFeature(GameFeature feature) {
        return () -> TotalRobotPoint.upgradeRabbitPointList(chooseBestPathToFeature(feature, startingLocation));
    }
    
    public Supplier<List<TotalRobotPoint>> getAdvancedAutoPathToScale() {
        if (matchingSide()) {
            log.info("Side matches - going to nearby scale plate");
            return () -> getAdvancedPathToNearbyScalePlate();
        }
        log.info("No match - going to distant scale plate");
        return () -> createAdvancedPathToDistantScalePlate();
    }
    
    public boolean matchingSide() {
        OwnedSide targetSide = gameData.getOwnedSide(GameFeature.SCALE);
        log.info("Target Side is: " + targetSide);
        
        boolean matchLeft = targetSide == OwnedSide.LEFT && startingLocation == StartingLocations.Left;
        boolean matchRight = targetSide == OwnedSide.RIGHT && startingLocation == StartingLocations.Right;
        boolean matchTotal = matchLeft || matchRight;
        log.info("Do we have a matching side?" + matchTotal);
        return (matchTotal);
    }
    
    public List<TotalRobotPoint> getAdvancedPathToNearbyScalePlate() {
        List<TotalRobotPoint> points = new ArrayList<>();

        OwnedSide targetSide = gameData.getOwnedSide(GameFeature.SCALE);
        log.info("Target Side is: " + targetSide);
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-3 * 12, 22 * 12), new ContiguousHeading(110)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        if (startingLocation == StartingLocations.Left) {
            points = mirrorTotalPointPath(points);
        }

        return points;
    }
    
    public List<TotalRobotPoint> getAdvancedPathToNearbyCubeFromScalePlate() {
        List<TotalRobotPoint> points = new ArrayList<>();
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-3 * 12, 20 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        points.add(new TotalRobotPoint(new RabbitPoint(new FieldPose(new XYPair(0, 0), new ContiguousHeading(250)),
                PointType.HeadingOnly, PointTerminatingType.Continue, PointDriveStyle.Macro), Gear.LOW_GEAR, 80));

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-3.5 * 12, 19 * 12), new ContiguousHeading(250)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Micro),
                Gear.LOW_GEAR, 80));

        if (startingLocation == StartingLocations.Left) {
            points = mirrorTotalPointPath(points);
        }

        return points;
    }
    
    public List<TotalRobotPoint> getAdvancedPathBackToScalePlateFromCube() {
        List<TotalRobotPoint> points = new ArrayList<>();

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-3 * 12, 19 * 12), new ContiguousHeading(180)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(90)),
                        PointType.HeadingOnly, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-3 * 12, 22 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.HIGH_GEAR, 100));

        if (startingLocation == StartingLocations.Left) {
            points = mirrorTotalPointPath(points);
        }

        return points;
    }
    
    

    private List<FieldPose> mirrorPath(List<FieldPose> path) {
        List<FieldPose> flippedPath = new ArrayList<FieldPose>();

        for (FieldPose pose : path) {

            double currentHeading = pose.getHeading().getValue();
            double flippedHeading = -1 * (currentHeading - 90) + 90;

            flippedPath.add(new FieldPose(new XYPair(-pose.getPoint().x, pose.getPoint().y),
                    new ContiguousHeading(flippedHeading)));
        }

        return flippedPath;
    }
    
    private List<TotalRobotPoint> mirrorTotalPointPath(List<TotalRobotPoint> path) {
        List<TotalRobotPoint> flippedPath = new ArrayList<TotalRobotPoint>();
        
        for(TotalRobotPoint point : path) {
            double currentHeading = point.simplePoint.pose.getHeading().getValue();
            double flippedHeading = -1 * (currentHeading - 90) + 90;
            flippedPath.add(new TotalRobotPoint(new RabbitPoint(
                        new FieldPose(
                                new XYPair(
                                        -point.simplePoint.pose.getPoint().x, 
                                        point.simplePoint.pose.getPoint().y), 
                                new ContiguousHeading(flippedHeading)), 
                        point.simplePoint.pointType, 
                        point.simplePoint.terminatingType,
                        point.simplePoint.driveStyle),
                point.desiredGear,
                point.velocityLimit));
        }    
        return flippedPath;
    }

    private List<RabbitPoint> chooseBestPathToFeature(GameFeature feature, StartingLocations whereToStart) {
        OwnedSide targetSide = gameData.getOwnedSide(feature);
        log.info("Target Side is: " + targetSide);
        List<FieldPose> bestPath = createPathToNowhere();
        switch (feature) {
        case SWITCH_NEAR:
            switch (targetSide) {
            case LEFT:
                log.info("Creating path to Left Switch Plate");
                if (whereToStart == StartingLocations.Right) {
                    bestPath = createPathToDistantSwitchPlate();
                } else if (whereToStart == StartingLocations.Left) {
                    bestPath = mirrorPath(createPathToNearbySwitchPlate());
                } else if (whereToStart == StartingLocations.Middle) {
                    bestPath = createPathToLeftSwitchPlateFromMiddle();
                }
                break;
            case RIGHT:
                log.info("Creating path to Right Switch Plate");
                if (whereToStart == StartingLocations.Right) {
                    bestPath = createPathToNearbySwitchPlate();
                } else if (whereToStart == StartingLocations.Left) {
                    bestPath = mirrorPath(createPathToDistantSwitchPlate());
                } else if (whereToStart == StartingLocations.Middle) {
                    bestPath = createPathToRightSwitchPlateFromMiddle();
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
                if (whereToStart == StartingLocations.Right) {
                    bestPath = createPathToDistantScalePlate();
                } else if (whereToStart == StartingLocations.Left) {
                    bestPath = mirrorPath(createPathToNearbyScalePlate());
                }
                break;
            case RIGHT:
                log.info("Creating path to Right Scale");
                if (whereToStart == StartingLocations.Right) {
                    bestPath = createPathToNearbyScalePlate();
                } else if (whereToStart == StartingLocations.Left) {
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

        return RabbitPoint.upgradeFieldPoseList(bestPath);
    }

    public List<FieldPose> createPathToNearbyScalePlate() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0 * 12, 18 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-1 * 12, 20.27 * 12), new ContiguousHeading(120)));
        return points;
    }

    public List<FieldPose> createPathToDistantScalePlate() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0 * 12, 14 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-16 * 12, 19 * 12), new ContiguousHeading(180)));
        points.add(new FieldPose(new XYPair(-16 * 12, 19 * 12), new ContiguousHeading(180)));
        points.add(new FieldPose(new XYPair(-18 * 12, 24 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-19 * 12, 20.27 * 12), new ContiguousHeading(70)));
        return points;
    }
    
    public List<TotalRobotPoint> createAdvancedPathToDistantScalePlate() {
        List<TotalRobotPoint> points = new ArrayList<>();
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, 14 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-16 * 12, 19 * 12), new ContiguousHeading(180)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-20.75 * 12, 25 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(0)),
                        PointType.HeadingOnly, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        
        if (startingLocation == StartingLocations.Left) {
            points = mirrorTotalPointPath(points);
        }
        
        return points;
    }

    public List<FieldPose> createPathToNearbySwitchPlate() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0 * 12, 1.5 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-6 * 12, 9 * 12), new ContiguousHeading(90)));
        return points;
    }

    public List<FieldPose> createPathToDistantSwitchPlate() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();

        points.add(new FieldPose(new XYPair(0 * 12, 1.5 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-7.35 * 12, 1.5 * 12), new ContiguousHeading(180)));
        points.add(new FieldPose(new XYPair(-15.6 * 12, 9 * 12), new ContiguousHeading(90)));
        return points;
    }

    private List<FieldPose> createPathToLeftSwitchPlateFromMiddle() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0 * 12, 1.5 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-6.5 * 12, 9 * 12), new ContiguousHeading(90)));
        return points;
    }

    private List<FieldPose> createPathToRightSwitchPlateFromMiddle() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0 * 12, 1.5 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(3.4 * 12, 9 * 12), new ContiguousHeading(90)));
        return points;
    }

    private List<FieldPose> createPathToNowhere() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0, 0), new ContiguousHeading(90)));
        return points;
    }

}
