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
    public DeviceInfo getLeftDriveMasterEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getRightDriveMasterEncoder() {
        return new DeviceInfo(0, false);
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
        return true;
    }

    @Override
    public DeviceInfo getWristMaster() {
        return new DeviceInfo(30, false);
    }

    @Override
    public DeviceInfo getWristEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public double getWristMaximumAngle() {
        return 90;
    }

    @Override
    public boolean collectorReady() {
        return true;
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
        return new DeviceInfo(33, false);
    }

    @Override
    public boolean climbDeployReady() {
        return false;
    }

    @Override
    public DeviceInfo getClimbDeployMaster() {
        return new DeviceInfo(22, false);
    }

    @Override
    public boolean climbReady() {
        return false;
    }

    @Override
    public DeviceInfo getClimbMaster() {
        return new DeviceInfo(23, false);
    }

    @Override
    public DeviceInfo getShifterSolenoid() {
        return new DeviceInfo(0, false);
    }

    @Override
    public boolean elevatorLowerLimitReady() {
        return true;
    }

    @Override
    public boolean elevatorUpperLimitReady() {
        return true;
    }

    @Override
    public DeviceInfo getElevatorUpperLimit() {
        return new DeviceInfo(1, true);
    }

    @Override
    public DeviceInfo getElevatorLowerLimit() {
        return new DeviceInfo(0, true);
    }

    @Override
    public boolean elevatorUsesTalonLimits() {
        return false;
    }

    @Override
    public DeviceInfo getElevatorEncoder() {
        return new DeviceInfo(0, true);
    }

    @Override
    public DeviceInfo getPawlSolenoidA() {
        return new DeviceInfo(1, false);
    }

    @Override
    public DeviceInfo getPawlSolenoidB() {
        return new DeviceInfo(2, false);
    }
}
