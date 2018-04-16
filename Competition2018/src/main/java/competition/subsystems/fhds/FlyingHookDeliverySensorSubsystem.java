package competition.subsystems.fhds;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.offboard.OffboardCommsConstants;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardFramePackingUtils;
import competition.subsystems.offboard.XOffboardCommsInterface;
import competition.subsystems.offboard.packets.DroneMotorCommandPacket;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XDigitalOutput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;

@Singleton
public class FlyingHookDeliverySensorSubsystem extends BaseSubsystem {
    
    private final Thread fhdsThread;

    private volatile double requestedPowerForward;
    private volatile double requestedPowerSideways;
    private volatile double requestedPowerUp;
    private volatile double requestedPowerYaw;
    
    private volatile boolean isFHDSRunning = false;
    private volatile Object fhdsThreadLock = new Object();
    
    private static class FHDSServoOutput {
        private final double FREQUENCY = 400;
        private final double PERIOD = 1 / FREQUENCY;
        
        XDigitalOutput output;
        public FHDSServoOutput(XDigitalOutput output) {
            this.output = output;
        }
        
        private double calculateDutyCycle(int pulseWidthMicros) {
            double targetTime = pulseWidthMicros / 1_000_000;
            return targetTime / PERIOD;
        }
        
        public void start(int pulseWidthMicros) {
            output.setPWMRate(FREQUENCY);
            output.enablePWM(calculateDutyCycle(pulseWidthMicros));
        }
        
        public void set(int pulseWidthMicros) {
            output.updateDutyCycle(calculateDutyCycle(pulseWidthMicros));
        }
        
        public void stop() {
            output.disablePWM();
        }
    }
    
    @Inject
    public FlyingHookDeliverySensorSubsystem(XOffboardCommsInterface comms, CommonLibFactory clf) {
        
        fhdsThread = new Thread(() -> {
            FHDSServoOutput leftFrontServo = new FHDSServoOutput(clf.createDigitalOutput(1));
            FHDSServoOutput rightFrontServo = new FHDSServoOutput(clf.createDigitalOutput(2));
            FHDSServoOutput leftRearServo = new FHDSServoOutput(clf.createDigitalOutput(3));
            FHDSServoOutput rightRearServo = new FHDSServoOutput(clf.createDigitalOutput(4));
            
            boolean isPwmStarted = false;
            while (!Thread.interrupted()) {
                if (!isFHDSRunning) {
                    try {
                        log.info("Stopping all FHDS PWMs");
                        isPwmStarted = false;
                        leftFrontServo.stop();
                        rightFrontServo.stop();
                        leftRearServo.stop();
                        rightRearServo.stop();
                        
                        log.info("FHDS thread waiting for continuation");
                        fhdsThreadLock.wait();
                        log.info("FHDS thread resumed after wait");
                    }
                    catch (InterruptedException e) {
                        continue;
                    }
                }
                
                OffboardCommunicationPacket packet = comms.receiveRaw(OffboardCommsConstants.SENDER_ID_DRONE_CONTROLLER);
                if (packet != null) {
                   if (packet.packetType == OffboardCommsConstants.PACKET_TYPE_DRONE_MOTOR_POWER) {
                       DroneMotorCommandPacket commandPacket = DroneMotorCommandPacket.parse(packet.data);
                       
                       if (isPwmStarted) {
                           leftFrontServo.set(commandPacket.leftFront);
                           rightFrontServo.set(commandPacket.rightFront);
                           leftRearServo.set(commandPacket.leftRear);
                           rightRearServo.set(commandPacket.rightRear);
                       }
                       else {
                           log.info("Starting all FHDS PWMs");
                           isPwmStarted = true;
                           leftFrontServo.start(commandPacket.leftFront);
                           rightFrontServo.start(commandPacket.rightFront);
                           leftRearServo.start(commandPacket.leftRear);
                           rightRearServo.start(commandPacket.rightRear);
                       }
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
        fhdsThread.start();
    }
    
    public void handleOperatorInput(XYPair leftVector, XYPair rightVector) {
        requestedPowerForward = leftVector.y;
        requestedPowerSideways = leftVector.x;
        requestedPowerYaw = rightVector.x;
        requestedPowerUp = rightVector.y;
    }
    
    public void startFHDSControl() {
        if (!isFHDSRunning) {
            log.info("Starting FHDS control");
            isFHDSRunning = true;
            fhdsThreadLock.notify();
        }
    }
    
    public void stopFHDSControl() {
        log.info("Stopping FHDS control");
        isFHDSRunning = false;
    }
}
