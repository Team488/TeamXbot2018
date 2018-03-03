package competition.subsystems.climb;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimbSubsystem extends BaseSubsystem {

    final DoubleProperty ascendSpeed;
    final DoubleProperty descendSpeed;
    CommonLibFactory clf;
    public XCANTalon motor;
    public XSolenoid solenoidA;
    public XSolenoid solenoidB;
    ElectricalContract2018 contract;
    final DoubleProperty absoluteMaxTicks;

    @Inject
    public ClimbSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        solenoidA = clf.createSolenoid(contract.getPawlSolenoidA().channel);
        solenoidB = clf.createSolenoid(contract.getPawlSolenoidB().channel);
        solenoidA.setInverted(contract.getPawlSolenoidA().inverted);
        solenoidB.setInverted(contract.getPawlSolenoidB().inverted);
        
        ascendSpeed = propMan.createPersistentProperty(getPrefix()+"AscendSpeed", 1);
        descendSpeed = propMan.createPersistentProperty(getPrefix()+"DescendSpeed", -.1);
        
        absoluteMaxTicks = propMan.createPersistentProperty(getPrefix() + "Absolute Max Ticks", 91202);

        if (contract.climbReady()) {
            initializeMotor();
        }
    }

    /**
     * Should only be called directly by test code. Temporary workaround to deal with the
     * "too-many-CAN-errors-crashes-robot" situation.
     */
    private void initializeMotor() {
        motor = clf.createCANTalon(contract.getClimbMaster().channel);
        motor.setInverted(contract.getClimbMaster().inverted);
        
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        motor.setSensorPhase(true);
    }

    /**
     * moves the winch to pull the robot up
     **/
    public void ascend() {
        setPower(ascendSpeed.get());
    }
    
    public void ascendLowPower() {
        setPower(ascendSpeed.get() / 5);
    }

    /**
     * moves the winch to let the robot down
     */
    public void descend() {
        setPower(descendSpeed.get());
    }
    
    public int getCurrentTicks() {
        return motor.getSelectedSensorPosition(0);
    }
    
    public void setPower(double power) {
        if (getCurrentTicks() < 0) {
            // too low, don't go down
            power = MathUtils.constrainDouble(power, 0, 1);
        }
        
        if (getCurrentTicks() >= absoluteMaxTicks.get()) {
            // too high, don't go up
            power = MathUtils.constrainDouble(power, -1, 0);
        }
        
        motor.simpleSet(power);
    }

    /**
     * stops the winch
     */
    public void stop() {
        motor.simpleSet(0);
    }
    
    public void releasePawl() {
        solenoidA.setOn(true);
        solenoidB.setOn(false);
    }
    
    public void engagePawl() {
        solenoidA.setOn(false);
        solenoidB.setOn(true);
    }
}
