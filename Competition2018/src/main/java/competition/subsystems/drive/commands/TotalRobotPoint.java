package competition.subsystems.drive.commands;

import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.math.FieldPose;

public class TotalRobotPoint {
    public FieldPose simplePoint;
    public Gear desiredGear;
    public double velocityLimit;
    
    public TotalRobotPoint(FieldPose simplePoint, Gear desiredGear, double velocityLimit) {
        this.simplePoint = simplePoint;
        this.desiredGear = desiredGear;
        this.velocityLimit = velocityLimit;
    }
}