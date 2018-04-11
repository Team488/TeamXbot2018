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
    
    //
    // Helper methods to prepare for autonomous
    // 

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
    
    public boolean matchingSide() {
        OwnedSide targetSide = gameData.getOwnedSide(GameFeature.SCALE);
        log.info("Target Side is: " + targetSide);

        boolean matchLeft = targetSide == OwnedSide.LEFT && startingLocation == StartingLocations.Left;
        boolean matchRight = targetSide == OwnedSide.RIGHT && startingLocation == StartingLocations.Right;
        boolean matchTotal = matchLeft || matchRight;
        log.info("Do we have a matching side?" + matchTotal);
        return (matchTotal);
    }

    private List<TotalRobotPoint> mirrorTotalPointPath(List<TotalRobotPoint> path) {
        return mirrorTotalPointPath(path, false);
    }
    
    private List<TotalRobotPoint> mirrorTotalPointPath(List<TotalRobotPoint> path, boolean relative) {
        List<TotalRobotPoint> flippedPath = new ArrayList<TotalRobotPoint>();

        for (TotalRobotPoint point : path) {
            double currentHeading = point.simplePoint.pose.getHeading().getValue();
            double flippedHeading = -1 * (currentHeading - 90) + 90;
            XYPair flippedPoint = new XYPair();
            if (relative) {
                // In relative mode, flip around 0,0
                flippedPoint = new XYPair(
                        -point.simplePoint.pose.getPoint().x,
                        point.simplePoint.pose.getPoint().y);
            } else {
                // In absolute mode, flip around the centerline of the field.
                // This would normally look like -(currentX-midline)+midline, but we can
                // just simplify to -currentX + 2*midline, or -currentX + fieldWidth.
                double fieldWidth = 27;
                flippedPoint = new XYPair(
                        -point.simplePoint.pose.getPoint().x + fieldWidth,
                        point.simplePoint.pose.getPoint().y);
            }            
            
            flippedPath.add(new TotalRobotPoint(
                    new RabbitPoint(
                            new FieldPose(
                                    flippedPoint,
                                    new ContiguousHeading(flippedHeading)),
                            point.simplePoint.pointType, point.simplePoint.terminatingType,
                            point.simplePoint.driveStyle),
                    point.desiredGear, point.velocityLimit));
        }
        return flippedPath;
    }
    
    //
    // Methods that return some kind of path
    //
    public enum SwitchScoringLocation {
        SideFacingAllianceWall,
        EndCaps,
        SideFacingScale
    }
    
    public List<TotalRobotPoint> getPathToCorrectSwitch(SwitchScoringLocation targetLocation) {

        switch (targetLocation) {
            case SideFacingAllianceWall:
                if (gameData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.RIGHT) {
                    // switch on right, we don't care about starting position
                    return createPathToRightSwitchPlateNearestEdge();
                }
                // switch on left, we don't care about starting position
                return createPathToLeftSwitchPlateNearestEdge();
            default:
                return createPathToNowhere();
        }
    }
    
    public List<TotalRobotPoint> createPathToRightSwitchPlateNearestEdge() {
        ArrayList<TotalRobotPoint> points = new ArrayList<>();
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(18 * 12, 9* 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        
        return points;
    }

    public List<TotalRobotPoint> createPathToLeftSwitchPlateNearestEdge() {
        return mirrorTotalPointPath(createPathToRightSwitchPlateNearestEdge());
    }
    
    public List<TotalRobotPoint> getPathToCorrectScale(SwitchScoringLocation targetLocation) {
        if (gameData.getOwnedSide(GameFeature.SCALE) == OwnedSide.RIGHT) {
            if (matchingSide()) {
                // scale on the right, we're on the right
                return createPathToRightAlignedScaleSafeScoringPosition();
            }
            // scale on the right, we're on the left
            return mirrorTotalPointPath(createPathToLeftUnalignedScaleSafeScoringPosition());
        } else {
            if (matchingSide()) {
                // scale on the left, we're on the left
                return mirrorTotalPointPath(createPathToRightAlignedScaleSafeScoringPosition());
            }
            // scale on the left, we're on the right
            return createPathToLeftUnalignedScaleSafeScoringPosition();
        }
    }    
    
    public List<TotalRobotPoint> createPathToRightAlignedScaleSafeScoringPosition() {
        ArrayList<TotalRobotPoint> points = new ArrayList<>();
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(22 * 12, 25* 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0, 0), new ContiguousHeading(180)),
                        PointType.HeadingOnly, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        
        return points;
    }
    
    public List<TotalRobotPoint> createPathToLeftUnalignedScaleSafeScoringPosition() {
        ArrayList<TotalRobotPoint> points = new ArrayList<>();
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(22 * 12, 14* 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(7 * 12, 19.5* 12), new ContiguousHeading(180)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(4 * 12, 25* 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0, 0), new ContiguousHeading(0)),
                        PointType.HeadingOnly, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        
        return points;
    }    
    
    // The rest of this class is commented for reference.

    /*
    
    public Supplier<List<TotalRobotPoint>> getAdvancedAutoPathToScale() {
        return () -> {
            if (matchingSide()) {
                log.info("Side matches - going to nearby scale plate");
                return getAdvancedPathToNearbyScalePlate();
            }
            log.info("No match - going to distant scale plate");
            return createAdvancedPathToDistantScalePlate();
        };
    }

    public List<TotalRobotPoint> getAdvancedPathToNearbyScalePlate() {
        List<TotalRobotPoint> points = new ArrayList<>();

        OwnedSide targetSide = gameData.getOwnedSide(GameFeature.SCALE);
        log.info("Target Side is: " + targetSide);

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, 20 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(2 * 12, 28 * 12), new ContiguousHeading(0)),
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
                new RabbitPoint(new FieldPose(new XYPair(-3.5 * 12, 19.5 * 12), new ContiguousHeading(250)),
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
                new RabbitPoint(new FieldPose(new XYPair(-3 * 12, 18.5 * 12), new ContiguousHeading(180)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(90)),
                        PointType.HeadingOnly, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-3 * 12, 20 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.HIGH_GEAR, 100));

        if (startingLocation == StartingLocations.Left) {
            points = mirrorTotalPointPath(points);
        }

        return points;
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
        points.add(new FieldPose(new XYPair(-16 * 12, 20 * 12), new ContiguousHeading(180)));
        points.add(new FieldPose(new XYPair(-18 * 12, 24 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-19 * 12, 20.27 * 12), new ContiguousHeading(70)));
        return points;
    }

    public List<TotalRobotPoint> createAdvancedPathToDistantScalePlate() {
        List<TotalRobotPoint> points = new ArrayList<>();

        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, 14.5 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-16 * 12, 20 * 12), new ContiguousHeading(180)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-21.75 * 12, 25 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-21.25 * 12, 25 * 12), new ContiguousHeading(0)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        if (startingLocation == StartingLocations.Left) {
            points = mirrorTotalPointPath(points);
        }

        return points;
    }

    private List<FieldPose> createPathToLeftSwitchPlateFromMiddle() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0 * 12, 1.5 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(-5.5 * 12, 9 * 12), new ContiguousHeading(90)));
        return points;
    }

    private List<FieldPose> createPathToRightSwitchPlateFromMiddle() {
        ArrayList<FieldPose> points = new ArrayList<FieldPose>();
        points.add(new FieldPose(new XYPair(0 * 12, 1.5 * 12), new ContiguousHeading(90)));
        points.add(new FieldPose(new XYPair(5 * 12, 9 * 12), new ContiguousHeading(90)));
        return points;
    } */

    private List<TotalRobotPoint> createPathToNowhere() {
        ArrayList<TotalRobotPoint> points = new ArrayList<>();
        
        points.add(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(13.5 * 12, 0 * 12), new ContiguousHeading(0)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        return points;
    }    
}
