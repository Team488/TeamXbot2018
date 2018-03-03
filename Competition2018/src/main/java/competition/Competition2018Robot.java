package competition;

public class Competition2018Robot extends Practice2018Robot {
    
    @Override
    public DeviceInfo getElevatorMaster() {
        return new DeviceInfo(25, false);
    }
    
    public DeviceInfo getElevatorEncoder() {
        return new DeviceInfo(0, false);
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
        return true;
    }
    
    @Override
    public boolean climbLeanReady() {
        return true;
    }
    
    @Override
    public boolean climbReady() {
        return true;
    }
    
    @Override
    public DeviceInfo getLeftCollectorMaster() {
        return new DeviceInfo(24, true);
    }
    
    @Override
    public boolean isWristLimitsReady() {
        return false;
    }
    
    @Override
    public double getWristMaximumAngle() {
        return 90;
    }
}
