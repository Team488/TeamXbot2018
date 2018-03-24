package competition.subsystems.drive.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.drive.PurePursuitCommand;
import xbot.common.subsystems.drive.PurePursuitCommand.RabbitChaseInfo;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class AbsolutePurePursuit2018Command extends PurePursuitCommand {

    public class TotalRobotPoint {
        public FieldPose simplePoint;
        public Gear desiredGear;
        public double powerLimit;
        
        public TotalRobotPoint(FieldPose simplePoint, Gear desiredGear, double powerLimit) {
            this.simplePoint = simplePoint;
            this.desiredGear = desiredGear;
            this.powerLimit = powerLimit;
        }
    }
    
    private final ShiftSubsystem shifter;
    private List<TotalRobotPoint> originalPoints;
    
    @Inject
    public AbsolutePurePursuit2018Command(ShiftSubsystem shifter, CommonLibFactory clf, BasePoseSubsystem pose, BaseDriveSubsystem drive,
            XPropertyManager propMan) {
        super(clf, pose, drive, propMan);
        
        this.shifter = shifter;
        originalPoints = new ArrayList<>();
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
        
        double maximumPower = originalPoints.get(pointIndex).powerLimit;
        
        // Small safety check - max power should never be zero, but let's just be cautious.
        if (maximumPower == 0) {
            maximumPower = 1;
        }
        
        double recommendedPower = Math.abs(chaseData.rotation) + Math.abs(chaseData.translation);
        if (recommendedPower > maximumPower) {
            double reductionRatio = recommendedPower / maximumPower;
            rotation /= reductionRatio;
            translation /= reductionRatio;            
        }
        
        drive.drive(new XYPair(0, translation), rotation);
        
        Gear desiredGear = originalPoints.get(pointIndex).desiredGear;
        if (shifter.getGear() != desiredGear) {
            shifter.setGear(desiredGear);
        }
    }

}
