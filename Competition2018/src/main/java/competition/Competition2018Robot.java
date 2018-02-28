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
}
