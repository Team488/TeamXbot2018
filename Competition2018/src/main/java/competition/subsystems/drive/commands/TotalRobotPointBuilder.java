package competition.subsystems.drive.commands;

import java.util.ArrayList;
import java.util.List;

import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.subsystems.drive.RabbitPointBuilder;

public class TotalRobotPointBuilder extends RabbitPointBuilder {

    public Gear desiredGear = Gear.LOW_GEAR;
    public double velocityLimit = 1000000;
    
    public List<TotalRobotPoint> points;
    
    public TotalRobotPointBuilder() {
        points = new ArrayList<>();
    }
    
    public List<TotalRobotPoint> buildTotalPoints() {
        return points;
    }
    
}
