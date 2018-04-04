package competition.subsystems.drive.commands;

import java.util.ArrayList;
import java.util.List;

import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.subsystems.drive.RabbitPoint;

public class TotalRobotPoint {
    public RabbitPoint simplePoint;
    public Gear desiredGear;
    public double velocityLimit;
    
    public TotalRobotPoint(RabbitPoint simplePoint, Gear desiredGear, double velocityLimit) {
        this.simplePoint = simplePoint;
        this.desiredGear = desiredGear;
        this.velocityLimit = velocityLimit;
    }
    
    public static List<TotalRobotPoint> upgradeRabbitPointList(List<RabbitPoint> oldPoints) {
        List<TotalRobotPoint> totalPoints = new ArrayList<>();
        oldPoints.stream().forEach(point -> totalPoints.add(new TotalRobotPoint(point, Gear.LOW_GEAR, 80)));
        return totalPoints;
    }
}