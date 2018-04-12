package competition.subsystems.fhds;

import com.google.inject.Singleton;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import xbot.common.command.BaseSubsystem;
import xbot.common.math.XYPair;

@Singleton
public class FlyingHookDeliverySubsystem extends BaseSubsystem {
    
    private final Thread droneThread;
    
    private final PWM droneMotorFrontLeft;
    private final PWM droneMotorFrontRight;
    private final PWM droneMotorBackLeft;
    private final PWM droneMotorBackRight;
    
    private final Joystick operatorController;
    
    public FlyingHookDeliverySubsystem() {
        droneMotorFrontRight = new PWM(1);
        droneMotorFrontLeft = new PWM(2);
        droneMotorBackLeft = new PWM(3);
        droneMotorBackRight = new PWM(4);
        
        operatorController = new Joystick(2);
        
        droneThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                int frontLeftMotorPWMValue = droneMotorFrontLeft.getRaw();
                int frontRightMotorPWMValue = droneMotorFrontRight.getRaw();
                int rearLeftMotorPWMValue = droneMotorBackLeft.getRaw();
                int rearRightMotorPWMValue = droneMotorBackRight.getRaw();
                sendRawPWMValuesToDrone(frontLeftMotorPWMValue, frontRightMotorPWMValue, 
                        rearLeftMotorPWMValue, rearRightMotorPWMValue);
            }
        });
    }
    
    private void sendRawPWMValuesToDrone(int frontLeft, int frontRight, int rearLeft, int rearRight) {
        droneMotorFrontLeft.setRaw(frontLeft);
        droneMotorFrontRight.setRaw(frontRight);
        droneMotorBackLeft.setRaw(rearLeft);
        droneMotorBackRight.setRaw(rearRight);
    }
    
    public void handleOperatorInput(XYPair leftVector, XYPair rightVector) {
        
    }
    
    public Thread getDroneThread() {
        return droneThread;
    }
}
