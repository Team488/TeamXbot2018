package competition;

import xbot.common.injection.RobotModule;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.math.XYPairManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;


public class TiltPreventionModule extends RobotModule{

    PoseSubsystem pose;
    DriveSubsystem drive;
    double goodPoint;
    double goodHeading;
    DoubleProperty yThrehold;
    DoubleProperty headingThrehold;


    @Inject
    public TiltPreventionModule(PoseSubsystem pose, DriveSubsystem drive,XPropertyManager propManager) {
        this.pose = pose;
        this.drive = drive;
        yThrehold = propManager.createPersistentProperty("Y-ThreholdForPreventingTilt", 10);
        headingThrehold = propManager.createPersistentProperty("HeadingThreholdForPreventingTilt", 0);
    }

    
    @Inject
    public FieldPose preventTilt(XYPair point, ContiguousHeading heading) {
        if(point.y > yThrehold.get() && headingThrehold.get() == heading.getValue() ) {
            return();
        }
        if (!headingThrehold.get() == heading.getValue()) {
            return ("Prevent Tilt Test does not apply, the input heading should be equal to the current heading");
        }
        
        return ();
        
     
    }

}

