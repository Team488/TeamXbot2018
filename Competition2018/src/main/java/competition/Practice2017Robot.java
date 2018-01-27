package competition;

import com.google.inject.Singleton;

@Singleton
public class Practice2017Robot extends Practice2018Robot {

    @Override
    public MotorInfo getLeftDriveMaster() {
        return new MotorInfo(20, true);
    }

    @Override
    public MotorInfo getLeftDriveFollower() {
        return new MotorInfo(21, true);
    }

    @Override
    public MotorInfo getRightDriveMaster() {
        return new MotorInfo(34, false);
    }

    @Override
    public MotorInfo getRightDriveFollower() {
        return new MotorInfo(35, false);
    }

    @Override
    public boolean elevatorReady() {
        return false;
    }

    @Override
    public boolean wristReady() {
        return false;
    }

    @Override
    public boolean collectorReady() {
        return false;
    }

    @Override
    public boolean climbLeanReady() {
        return false;
    }
    
    @Override
    public boolean climbExtendReady() {
        return false;
    }
    
    @Override
    public boolean climbReady() {
        return false;
    }
}
