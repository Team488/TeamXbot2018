package competition.subsystems.drive.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.VelocityThrottleModule;
import xbot.common.math.FieldPose;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.PurePursuitCommand;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class AbsolutePurePursuit2018Command extends PurePursuitCommand {
    
    private final ShiftSubsystem shifter;
    private List<TotalRobotPoint> originalPoints;
    final VelocityThrottleModule translationalThrottleModule;
    final DoubleProperty maxInchesPerSecondProp;
    private final DriveSubsystem drive;
    
    @Inject
    public AbsolutePurePursuit2018Command(ShiftSubsystem shifter, CommonLibFactory clf, BasePoseSubsystem pose, DriveSubsystem drive,
            XPropertyManager propMan) {
        super(clf, pose, drive, propMan);
        this.drive = drive;
        this.shifter = shifter;
        
        translationalThrottleModule =
                clf.createVelocityThrottleModule(getPrefix() + "Translational", drive.getTranslationalVelocityPid());
        maxInchesPerSecondProp = propMan.createPersistentProperty(getPrefix() + "MaxInchesPerSecond", 120);
        
        originalPoints = new ArrayList<>();
    }
    
    public void addPoint(TotalRobotPoint point) {
        originalPoints.add(point);
    }

    @Override
    protected List<FieldPose> getOriginalPoints() {
        ArrayList<FieldPose> simplePoints = new ArrayList<>();
        originalPoints.stream().forEach(complexPoint -> simplePoints.add(complexPoint.simplePoint));
        return simplePoints;        
    }

    @Override
    protected PursuitMode getPursuitMode() {
        return PursuitMode.Absolute;
    }
    
    @Override
    public void execute() {
        // In the base command, the program would get the chaseData and apply it to the drive. We're mostly
        // okay with this, but we want to potentially modify the total power output as well as shift gears.
        RabbitChaseInfo chaseData = navigateToRabbit();
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
    }

}
