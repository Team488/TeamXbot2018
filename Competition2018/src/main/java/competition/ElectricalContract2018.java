package competition;

public abstract class ElectricalContract2018 {

    public class DeviceInfo {
        public int channel;
        public boolean inverted;

        public DeviceInfo(int channel, boolean inverted) {
            this.channel = channel;
            this.inverted = inverted;
        }
    }

    // Drive motors
    public abstract DeviceInfo getLeftDriveMaster();

    public abstract DeviceInfo getLeftDriveFollower();

    public abstract DeviceInfo getRightDriveMaster();

    public abstract DeviceInfo getRightDriveFollower();

    public abstract DeviceInfo getLeftDriveMasterEncoder();

    public abstract DeviceInfo getRightDriveMasterEncoder();

    // Pneumatic Shifters
    public abstract DeviceInfo getShifterSolenoid();

    // Elevator motor
    public abstract boolean elevatorReady();

    public abstract DeviceInfo getElevatorMaster();

    public abstract DeviceInfo getElevatorEncoder();
    
    // Elevator sensor
    public abstract boolean elevatorLowerLimitReady();

    public abstract boolean elevatorUpperLimitReady();

    public abstract boolean elevatorUsesTalonLimits();

    public abstract DeviceInfo getElevatorLowerLimit();

    public abstract DeviceInfo getElevatorUpperLimit();

    // Wrist Motor
    public abstract boolean wristReady();

    public abstract DeviceInfo getWristMaster();

    public abstract DeviceInfo getWristEncoder();

    public abstract double getWristMaximumAngle();

    // Collector Motors
    public abstract boolean collectorReady();

    public abstract DeviceInfo getLeftCollectorMaster();

    public abstract DeviceInfo getRightCollectorMaster();

    // Climb Lean
    public abstract boolean climbLeanReady();

    public abstract DeviceInfo getClimbLeanMaster();

    // Climb extend
    public abstract boolean climbDeployReady();

    public abstract DeviceInfo getClimbDeployMaster();

    // Climber
    public abstract boolean climbReady();

    public abstract DeviceInfo getClimbMaster();

    public abstract DeviceInfo getPawlSolenoidA();
    
    public abstract DeviceInfo getPawlSolenoidB();
}
