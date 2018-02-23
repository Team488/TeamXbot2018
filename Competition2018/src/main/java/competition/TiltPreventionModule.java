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
    DoubleProperty pitchThrehold;


    @Inject
    public TiltPreventionModule(PoseSubsystem pose, DriveSubsystem drive,XPropertyManager propManager) {
        this.pose = pose;
        this.drive = drive;
        pitchThrehold = propManager.createPersistentProperty("Absolute Value of Pitch Threhold", 10);
    }

    @Inject
    public void preventTilt (double yMaxPositiveSpeed , double yMaxNegativeSpeed) {
        
        if (pose.getRobotPitch() > pitchThrehold.get()) {

            yMaxPositiveSpeed = 0 ;
        }

        if(pose.getRobotPitch() < -pitchThrehold.get()) {
            
            yMaxNegativeSpeed = 0 ;
        }
        
        if(-pitchThrehold.get() <= pose.getRobotPitch() && pose.getRobotPitch() <= pitchThrehold.get()) {
            
            yMaxPositiveSpeed = 1;
            yMaxNegativeSpeed = -1;
            
        }
    }
}