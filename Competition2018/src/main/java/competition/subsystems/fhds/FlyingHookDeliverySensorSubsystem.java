package competition.subsystems.fhds;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import competition.subsystems.offboard.OffboardCommsConstants;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardFramePackingUtils;
import competition.subsystems.offboard.XOffboardCommsInterface;
import competition.subsystems.offboard.packets.DroneMotorCommandPacket;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XPWM;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;

@Singleton
public class FlyingHookDeliverySensorSubsystem extends BaseSubsystem {
    
    private final Thread droneThread;

    private volatile double requestedPowerForward;
    private volatile double requestedPowerSideways;
    private volatile double requestedPowerUp;
    private volatile double requestedPowerYaw;
    
    private volatile boolean isDroneRunning = false;
    private volatile Object droneThreadLock = new Object();
    
    @Inject
    public FlyingHookDeliverySensorSubsystem(XOffboardCommsInterface comms, CommonLibFactory clf) {
        
        droneThread = new Thread(() -> {
            XPWM leftFrontMotor = clf.createPWM(1);
            XPWM rightFrontMotor = clf.createPWM(2);
            XPWM leftRearMotor = clf.createPWM(3);
            XPWM rightRearMotor = clf.createPWM(4);
            
            while (!Thread.interrupted()) {
                if (!isDroneRunning) {
                    try {
                        log.info("Drone thread waiting for continuation");
                        droneThreadLock.wait();
                        log.info("Drone thread resumed after wait");
                    }
                    catch (InterruptedException e) {
                        continue;
                    }
                }
                
                OffboardCommunicationPacket packet = comms.receiveRaw(OffboardCommsConstants.SENDER_ID_DRONE_CONTROLLER);
                if (packet != null) {
                   if (packet.packetType == OffboardCommsConstants.PACKET_TYPE_DRONE_MOTOR_POWER) {
                       DroneMotorCommandPacket commandPacket = DroneMotorCommandPacket.parse(packet.data);

                       leftFrontMotor.setSigned(commandPacket.leftFront);
                       rightFrontMotor.setSigned(commandPacket.rightFront);
                       leftRearMotor.setSigned(commandPacket.leftRear);
                       rightRearMotor.setSigned(commandPacket.rightRear);
                   }
                   else {
                       log.warn("Received unknown packet in FHDS loop");
                   }
                }
                
                comms.sendRaw(
                        OffboardCommsConstants.PACKET_TYPE_DRONE_CONTROL_INPUT,
                        OffboardFramePackingUtils.packDroneCommandFrame(
                                requestedPowerForward,
                                requestedPowerSideways,
                                requestedPowerUp,
                                requestedPowerYaw));
            }
        });
        
        droneThread.start();
    }
    
    public void handleOperatorInput(XYPair leftVector, XYPair rightVector) {
        requestedPowerForward = leftVector.y;
        requestedPowerSideways = leftVector.x;
        requestedPowerYaw = rightVector.x;
        requestedPowerUp = rightVector.y;
    }
    
    public void startDroneControl() {
        if (!isDroneRunning) {
            log.info("Starting drone control");
            isDroneRunning = true;
            droneThreadLock.notify();
        }
    }
    
    public void stopDroneControl() {
        log.info("Stopping drone control");
        isDroneRunning = false;
    }
}
