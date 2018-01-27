package competition;

import com.google.inject.Singleton;

@Singleton
public class Practice2017Robot extends Practice2018Robot {

    @Override
    public DeviceInfo getLeftDriveMaster() {
        return new DeviceInfo(20, true);
    }

    @Override
    public DeviceInfo getLeftDriveFollower() {
        return new DeviceInfo(21, true);
    }

    @Override
    public DeviceInfo getRightDriveMaster() {
        return new DeviceInfo(34, false);
    }

    @Override
    public DeviceInfo getRightDriveFollower() {
        return new DeviceInfo(35, false);
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
    public boolean climbDeployReady() {
        return false;
    }
    
    @Override
    public boolean climbReady() {
        return false;
    }
}
