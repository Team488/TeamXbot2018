package competition;

import com.google.inject.Singleton;

@Singleton
public class Practice2018Robot extends ElectricalContract2018 {

    @Override
    public DeviceInfo getLeftDriveMaster() {
        return new DeviceInfo(34, true);
    }

    @Override
    public DeviceInfo getLeftDriveFollower() {
        return new DeviceInfo(35, true);
    }

    @Override
    public DeviceInfo getRightDriveMaster() {
        return new DeviceInfo(21, false);
    }

    @Override
    public DeviceInfo getRightDriveFollower() {
        return new DeviceInfo(20, false);
    }

    @Override
    public boolean elevatorReady() {
        return true;
    }

    @Override
    public DeviceInfo getElevatorMaster() {
        return new DeviceInfo(25, true);
    }

    @Override
    public boolean wristReady() {
        return false;
    }

    @Override
    public DeviceInfo getWristMaster() {
        return new DeviceInfo(2, false);
    }

    @Override
    public boolean collectorReady() {
        return false;
    }

    @Override
    public DeviceInfo getLeftCollectorMaster() {
        return new DeviceInfo(24, false);
    }

    @Override
    public DeviceInfo getRightCollectorMaster() {
        return new DeviceInfo(31, false);
    }

    @Override
    public boolean climbLeanReady() {
        return false;
    }

    @Override
    public DeviceInfo getClimbLeanMaster() {
        return new DeviceInfo(5, false);
    }

    @Override
    public boolean climbDeployReady() {
        return false;
    }

    @Override
    public DeviceInfo getClimbDeployMaster() {
        return new DeviceInfo(6, false);
    }

    @Override
    public boolean climbReady() {
        return false;
    }

    @Override
    public DeviceInfo getClimbMaster() {
        return new DeviceInfo(7, false);
    }

    @Override
    public DeviceInfo getShifterSolenoid() {
        return new DeviceInfo(1, false);
    }

    @Override
    public boolean elevatorLowerLimitReady() {
        return false;
    }

    @Override
    public DeviceInfo getElevatorLowerLimit() {
        return new DeviceInfo(1, false);
    }

}
