package competition;

public class Competition2018Robot extends Practice2018Robot {

    @Override
    public boolean elevatorReady() {
        return false;
    }
    
    @Override
    public boolean elevatorLowerLimitReady() {
        return false;
    }
    
    @Override
    public boolean elevatorUpperLimitReady() {
        return false;
    }
    
    @Override
    public boolean wristReady() {
        return false;
    }
    
    @Override
    public DeviceInfo getShifterSolenoid() {
        return new DeviceInfo(0, false);
    }
}
