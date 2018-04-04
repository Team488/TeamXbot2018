package competition.subsystems.drive.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.VelocityThrottleModule;
import xbot.common.math.FieldPose;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.PurePursuitCommand;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class AbsolutePurePursuit2018Command extends PurePursuitCommand {
    
    private final ShiftSubsystem shifter;
    private List<TotalRobotPoint> originalPoints;
    private Supplier<List<TotalRobotPoint>> externalPointSource;
    final VelocityThrottleModule translationalThrottleModule;
    final DoubleProperty maxInchesPerSecondProp;
    private final DriveSubsystem drive;
    private final PoseSubsystem pose;
    
    @Inject
    public AbsolutePurePursuit2018Command(ShiftSubsystem shifter, CommonLibFactory clf, PoseSubsystem pose, DriveSubsystem drive,
            XPropertyManager propMan) {
        super(clf, pose, drive, propMan);
        this.drive = drive;
        this.pose = pose;
        this.shifter = shifter;
        
        translationalThrottleModule =
                clf.createVelocityThrottleModule(getPrefix() + "Translational", drive.getTranslationalVelocityPid());
        maxInchesPerSecondProp = propMan.createPersistentProperty(getPrefix() + "MaxInchesPerSecond", 120);
        
        originalPoints = new ArrayList<>();
    }
    
    public void addPoint(TotalRobotPoint point) {
        originalPoints.add(point);
    }
    
    public void setAllPoints(List<TotalRobotPoint> points) {
        originalPoints = points;
    }
    
    public void setPointSupplier(Supplier<List<TotalRobotPoint>> externalPointSource) {
        this.externalPointSource = externalPointSource;
    }
    
    protected List<RabbitPoint> getOriginalPoints() {
        if (externalPointSource != null) {
            originalPoints = externalPointSource.get();
        }
        return downgradeTotalRobotPoints(originalPoints);
    }

    private List<RabbitPoint> downgradeTotalRobotPoints(List<TotalRobotPoint> complexPoints) {
        ArrayList<RabbitPoint> simplePoints = new ArrayList<>();
        complexPoints.stream().forEach(complexPoint -> simplePoints.add(complexPoint.simplePoint));
        return simplePoints;        
    }

    @Override
    protected PointLoadingMode getPursuitMode() {
        return PointLoadingMode.Absolute;
    }
    
    @Override
    public void execute() {
        // In the base command, the program would get the chaseData and apply it to the drive. We're mostly
        // okay with this, but we want to potentially modify the total power output as well as shift gears.

        if (originalPoints.size() == 0) {
            return;
        }
        
        RabbitChaseInfo chaseData = evaluateCurrentPoint();
        double rotation = chaseData.rotation;
        double translation = chaseData.translation;
        
        double maximumSpeed = originalPoints.get(pointIndex).velocityLimit;
        
        double translateSpeedGoal = translation * maxInchesPerSecondProp.get();
        double coercedSpeedGoal = MathUtils.constrainDouble(translateSpeedGoal, -maximumSpeed, maximumSpeed);
        
        double translationalPower = translationalThrottleModule.calculateThrottle(
                coercedSpeedGoal, 
                drive.getVelocityInInchesPerSecond());
        
        drive.drive(new XYPair(0, translationalPower), rotation);
        
        Gear desiredGear = originalPoints.get(pointIndex).desiredGear;
        if (shifter.getGear() != desiredGear) {
            shifter.setGear(desiredGear);
        }
        
        logTelemetry(
                pose.getCurrentFieldPose(), 
                chaseData.target, 
                chaseData.rabbit, 
                drive.getVelocityInInchesPerSecond(), 
                coercedSpeedGoal);
    }
    
    private void logTelemetry(FieldPose robot, FieldPose target, FieldPose rabbit, double velocity, double velocityGoal) {
        List<Double> data = new ArrayList<>();
        data.add(Timer.getFPGATimestamp());
        data.add(robot.getPoint().x);
        data.add(robot.getPoint().y);
        data.add(robot.getHeading().getValue());
        data.add(target.getPoint().x);
        data.add(target.getPoint().y);
        data.add(target.getHeading().getValue());
        data.add(rabbit.getPoint().x);
        data.add(rabbit.getPoint().y);
        data.add(velocityGoal);
        data.add(velocity);
        
        String formattedData = data.stream().map(d -> String.format("%.2f", d)).collect(Collectors.joining(","));
        log.info(formattedData);
    }
    
}
