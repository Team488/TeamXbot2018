package competition;

public abstract class ElectricalContract {
    
    public class MotorInfo {
        public int channel;
        public boolean inverted;
        
        public MotorInfo(int channel, boolean inverted) {
            this.channel = channel;
            this.inverted = inverted;
        }
    }

    // Drive motors
    public abstract MotorInfo getLeftDriveMaster();
    public abstract MotorInfo getLeftDriveFollower();
    
    public abstract MotorInfo getRightDriveMaster();
    public abstract MotorInfo getRightDriveFollower();
    
    // Elevator motor
    public abstract boolean elevatorReady();
    public abstract MotorInfo getElevatorMaster();
    
    // Wrist Motor
    public abstract boolean wristReady();
    public abstract MotorInfo getWristMaster();
    
    // Collector Motors
    public abstract boolean collectorReady();
    public abstract MotorInfo getLeftCollectorMaster();
    public abstract MotorInfo getRightCollectorMaster();
    
    // Climb Lean
    public abstract boolean climbLeanReady();
    public abstract MotorInfo getClimbLeanMaster();
    
    // Climb extend
    public abstract boolean climbExtendReady();
    public abstract MotorInfo getClimbExtendMaster();
    
    // Climber
    public abstract boolean climbReady();
    public abstract MotorInfo getClimbMaster();
}
