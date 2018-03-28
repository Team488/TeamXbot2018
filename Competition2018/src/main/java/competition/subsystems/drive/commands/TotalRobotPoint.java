package competition.subsystems.drive.commands;

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
}