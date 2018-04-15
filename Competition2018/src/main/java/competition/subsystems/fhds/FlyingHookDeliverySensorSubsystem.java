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
import xbot.common.controls.actuators.XPWM;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;

@Singleton
public class FlyingHookDeliverySensorSubsystem extends BaseSubsystem {
    
    private final Thread fhdsThread;

    private volatile double requestedPowerForward;
    private volatile double requestedPowerSideways;
    private volatile double requestedPowerUp;
    private volatile double requestedPowerYaw;
    
    private volatile boolean isDroneRunning = false;
    private volatile Object droneThreadLock = new Object();
    
    private static class FHDSMotorOutput {
        // Maximum frequency is (1000 (ms/s) / 2 ms) = 500Hz, but we need to
        // ensure there is margin between each pulse so they are individually
        // discernible. Division factor is chosen arbitrarily.
        private final double FREQUENCY = (1000d/2d) / 1.5;
        private final double PERIOD = 1/FREQUENCY;
        
        XDigitalOutput output;
        public FHDSMotorOutput(XDigitalOutput output) {
            this.output = output;
        }
        
        private double calculateDutyCycle(double throttle) {
            double targetTime = MathUtils.scaleDouble(throttle, -1, 1, 1.0, 2.0) / 1000;
            return targetTime / PERIOD;
        }
        
        public void start(double throttle) {
            output.setPWMRate(FREQUENCY);
            output.enablePWM(calculateDutyCycle(throttle));
        }
        
        public void set(double throttle) {
            output.updateDutyCycle(calculateDutyCycle(throttle));
        }
        
        public void stop() {
            output.disablePWM();
        }
    }
    
    @Inject
    public FlyingHookDeliverySensorSubsystem(XOffboardCommsInterface comms, CommonLibFactory clf) {
        
        fhdsThread = new Thread(() -> {
            FHDSMotorOutput leftFrontMotor = new FHDSMotorOutput(clf.createDigitalOutput(1));
            FHDSMotorOutput rightFrontMotor = new FHDSMotorOutput(clf.createDigitalOutput(2));
            FHDSMotorOutput leftRearMotor = new FHDSMotorOutput(clf.createDigitalOutput(3));
            FHDSMotorOutput rightRearMotor = new FHDSMotorOutput(clf.createDigitalOutput(4));
            
            boolean isPwmStarted = false;
            while (!Thread.interrupted()) {
                if (!isDroneRunning) {
                    try {
                        log.info("Stopping all FHDS PWMs");
                        isPwmStarted = false;
                        leftFrontMotor.stop();
                        rightFrontMotor.stop();
                        leftRearMotor.stop();
                        rightRearMotor.stop();
                        
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
                       
                       if (isPwmStarted) {
                           leftFrontMotor.set(commandPacket.leftFront);
                           rightFrontMotor.set(commandPacket.rightFront);
                           leftRearMotor.set(commandPacket.leftRear);
                           rightRearMotor.set(commandPacket.rightRear);
                       }
                       else {
                           log.info("Starting all FHDS PWMs");
                           isPwmStarted = true;
                           leftFrontMotor.start(commandPacket.leftFront);
                           rightFrontMotor.start(commandPacket.rightFront);
                           leftRearMotor.start(commandPacket.leftRear);
                           rightRearMotor.start(commandPacket.rightRear);
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
        if (!isDroneRunning) {
            log.info("Starting drone control");
            isDroneRunning = true;
            droneThreadLock.notify();
        }
    }
    
    public void stopFHDSControl() {
        log.info("Stopping drone control");
        isDroneRunning = false;
    }
}
