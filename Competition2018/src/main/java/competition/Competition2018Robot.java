package competition;

public class Competition2018Robot extends ElectricalContract2018 {
    
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
        return true;
    }
    
    @Override
    public boolean collectorReady() {
        return true;
    }
    
    @Override
    public boolean elevatorLowerLimitReady() {
        return true;
    }
    
    @Override
    public boolean elevatorReady() {
        return true;
    }
    
    @Override
    public boolean elevatorUpperLimitReady() {
        return true;
    }
    
    @Override
    public boolean elevatorUsesTalonLimits() {
        return false;
    }
    
    @Override
    public DeviceInfo getAutonomousLight() {
        return new DeviceInfo(100, false);
    }
    
    @Override
    public DeviceInfo getClimbDeployMaster() {
        return new DeviceInfo(50, true);
    }
    
    @Override
    public DeviceInfo getClimbLeanMaster() {
        return new DeviceInfo(55, true);
    }
    
    @Override
    public DeviceInfo getClimbMaster() {
        return new DeviceInfo(33, true);
    }

    public DeviceInfo getElevatorEncoder() {
        return new DeviceInfo(0, true);
    }

    @Override
    public DeviceInfo getElevatorFollower() {
        return new DeviceInfo(22, false);
    }
    
    @Override
    public DeviceInfo getElevatorLowerLimit() {
        return new DeviceInfo(1, true);
    }
    @Override
    public DeviceInfo getElevatorMaster() {
        return new DeviceInfo(25, true);
    }

    @Override
    public DeviceInfo getElevatorUpperLimit() {
        return new DeviceInfo(0, true);
    }

    @Override
    public DeviceInfo getLeftCollectorMaster() {
        return new DeviceInfo(24, true);
    }

    @Override
    public DeviceInfo getLeftDriveFollower() {
        return new DeviceInfo(35, true);
    }

    @Override
    public DeviceInfo getLeftDriveMaster() {
        return new DeviceInfo(34, true);
    }

    @Override
    public DeviceInfo getLeftDriveMasterEncoder() {
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
    public DeviceInfo getRightCollectorMaster() {
        return new DeviceInfo(31, false);
    }

    @Override
    public DeviceInfo getRightDriveFollower() {
        return new DeviceInfo(20, false);
    }

    @Override
    public DeviceInfo getRightDriveMaster() {
        return new DeviceInfo(21, false);
    }

    @Override
    public DeviceInfo getRightDriveMasterEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getShifterSolenoid() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getWristEncoder() {
        return new DeviceInfo(0, false);
    }

    @Override
    public DeviceInfo getWristLowerLimit() {
        return new DeviceInfo(3, true);
    }

    @Override
    public DeviceInfo getWristMaster() {
        return new DeviceInfo(30, false);
    }

    @Override
    public double getWristMaximumAngle() {
        return 90;
    }

    @Override
    public DeviceInfo getWristUpperLimit() {
        return new DeviceInfo(2, true);
    }

    @Override
    public DeviceInfo getZedDeploy() {
        return new DeviceInfo(0, false);
    }

    @Override
    public boolean isWristLimitsReady() {
        return false;
    }

    @Override
    public boolean wristReady() {
        return true;
    }
}
