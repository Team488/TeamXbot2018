package competition;

import competition.ElectricalContract2018.DeviceInfo;

public class Competition2018Robot extends Practice2018Robot {
    
    @Override
    public DeviceInfo getElevatorMaster() {
        return new DeviceInfo(25, true);
    }
    
    @Override
    public DeviceInfo getElevatorFollower() {
        return new DeviceInfo(22, false);
    }
    
    public DeviceInfo getElevatorEncoder() {
        return new DeviceInfo(0, true);
    }
    
    @Override
    public DeviceInfo getShifterSolenoid() {
        return new DeviceInfo(0, false);
    }
    
    @Override
    public DeviceInfo getPawlSolenoidA() {
        return new DeviceInfo(1, true);
    }
    
    @Override
    public DeviceInfo getPawlSolenoidB() {
        return new DeviceInfo(2, true);
    }
    
    @Override
    public boolean climbDeployReady() {
        return false;
    }
    
    @Override
    public boolean climbLeanReady() {
        return false;
    }
    
    @Override
    public boolean climbReady() {
        return false;
    }
    
    @Override
    public boolean isWristLimitsReady() {
        return false;
    }
    
    @Override
    public double getWristMaximumAngle() {
        return 90;
    }
    
    @Override
    public DeviceInfo getElevatorUpperLimit() {
        return new DeviceInfo(0, true);
    }

    @Override
    public DeviceInfo getElevatorLowerLimit() {
        return new DeviceInfo(1, true);
    }

    @Override
    public DeviceInfo getLeftCollectorMaster() {
        return new DeviceInfo(24, true);
    }
    
    @Override
    public DeviceInfo getRightCollectorMaster() {
        return new DeviceInfo(31, false);
    }
}
