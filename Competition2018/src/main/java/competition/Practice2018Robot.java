package competition;

import com.google.inject.Singleton;

@Singleton
public class Practice2018Robot extends ElectricalContract2018 {

    @Override
    public MotorInfo getLeftDriveMaster() {
        return new MotorInfo(34, true);
    }

    @Override
    public MotorInfo getLeftDriveFollower() {
        return new MotorInfo(35, true);
    }

    @Override
    public MotorInfo getRightDriveMaster() {
        return new MotorInfo(21, false);
    }

    @Override
    public MotorInfo getRightDriveFollower() {
        return new MotorInfo(20, false);
    }

    @Override
    public boolean elevatorReady() {
        return false;
    }

    @Override
    public MotorInfo getElevatorMaster() {
        return new MotorInfo(25, false);
    }

    @Override
    public boolean wristReady() {
        return false;
    }

    @Override
    public MotorInfo getWristMaster() {
        return new MotorInfo(2, false);
    }

    @Override
    public boolean collectorReady() {
        return false;
    }

    @Override
    public MotorInfo getLeftCollectorMaster() {
        return new MotorInfo(34,false);
    }

    @Override
    public MotorInfo getRightCollectorMaster() {
        return new MotorInfo(31, false);
    }

    @Override
    public boolean climbLeanReady() {
        return false;
    }

    @Override
    public MotorInfo getClimbLeanMaster() {
        return new MotorInfo(5, false);
    }

    @Override
    public boolean climbExtendReady() {
        return false;
    }

    @Override
    public MotorInfo getClimbExtendMaster() {
        return new MotorInfo(6, false);
    }

    @Override
    public boolean climbReady() {
        return false;
    }

    @Override
    public MotorInfo getClimbMaster() {
        return new MotorInfo(7, false);
    }

}
