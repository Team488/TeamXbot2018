package competition;

public class Practice2017Platform extends ElectricalContract {

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
    public MotorInfo getElevatorMaster() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean wristReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MotorInfo getWristMaster() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean collectorReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MotorInfo getLeftCollectorMaster() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MotorInfo getRightCollectorMaster() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean climbLeanReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MotorInfo getClimbLeanMaster() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean climbExtendReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MotorInfo getClimbExtendMaster() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean climbReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MotorInfo getClimbMaster() {
        // TODO Auto-generated method stub
        return null;
    }

}
