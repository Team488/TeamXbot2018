package competition;

import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import com.google.inject.Inject;
import competition.subsystems.pose.PoseSubsystem;


public class TiltPreventionModule{

    PoseSubsystem pose;
    double goodPoint;
    double goodHeading;
    DoubleProperty pitchThrehold;


    @Inject
    public TiltPreventionModule(PoseSubsystem pose, XPropertyManager propManager) {
        this.pose = pose;
        pitchThrehold = propManager.createPersistentProperty("Absolute Value of Pitch Threhold", 10);
    }

    public double preventTilt (double potentialPower) {

        if (pose.getRobotPitch() > pitchThrehold.get() && potentialPower < 0) {
            return 0; 
        }

        if(pose.getRobotPitch() < -pitchThrehold.get() && potentialPower > 0) {
            return 0 ; 
        }

        return potentialPower;
    }
}