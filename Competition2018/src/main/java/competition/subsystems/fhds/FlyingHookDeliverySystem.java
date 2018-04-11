package competition.subsystems.fhds;

import edu.wpi.first.wpilibj.PWM;
import xbot.common.command.BaseSubsystem;

public class FlyingHookDeliverySystem extends BaseSubsystem implements Runnable {
    
    private PWM droneMotorFrontLeft;
    private PWM droneMotorFrontRight;
    private PWM droneMotorBackLeft;
    private PWM droneMotorBackRight;
    
    private boolean droneEnabled = false;
    
    public FlyingHookDeliverySystem() {
        droneMotorFrontRight = new PWM(1);
        droneMotorFrontLeft = new PWM(2);
        droneMotorBackLeft = new PWM(3);
        droneMotorFrontRight = new PWM(4);
    }
    
    @Override
    public void run() {
        while (droneEnabled) {
            
        }
    }
    
    public void setDroneMotorSpeeds(double frontLeft, double frontRight, double rearLeft, double rearRight) {
        droneMotorFrontLeft.setSpeed(frontLeft);
        droneMotorFrontRight.setSpeed(frontRight);
        droneMotorBackLeft.setSpeed(rearLeft);
        droneMotorBackRight.setSpeed(rearRight);
    }
}
