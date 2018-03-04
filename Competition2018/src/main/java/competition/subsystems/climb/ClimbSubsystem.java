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
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimbSubsystem extends BaseSubsystem {

    public enum ClimbRestrictionReason {
        TooMuchStrapOut,
        TooCloseToWinch,
        FullPowerAvailable
    }
    
    final DoubleProperty ascendSpeed;
    final DoubleProperty descendSpeed;
    final StringProperty climbRestrictionProp;
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
        climbRestrictionProp = propMan.createEphemeralProperty(getPrefix() + "Restriction Reason", "Not set yet");
        
        absoluteMaxTicks = propMan.createPersistentProperty(getPrefix() + "Absolute Max Ticks", -91202);

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
        
        motor.createTelemetryProperties(getPrefix(), "Motor");
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
        ClimbRestrictionReason potentialReason = ClimbRestrictionReason.FullPowerAvailable;
        
        // positive power climbs
        // climbing makes the sensor more negative
        // we start at zero
        
        // if we start at zero, then we don't want to allow very much positive motion
        // if we are too positivce, then we should only allow positive power
        
        if (getCurrentTicks() < absoluteMaxTicks.get()) {
            // too much cable out, don't pay out any more cable, no more descend
            power = MathUtils.constrainDouble(power, 0, 1);
            potentialReason = ClimbRestrictionReason.TooMuchStrapOut;
        }
        
        if (getCurrentTicks() > 0) {
            // hook getting too close to winch - no more ascend.
            power = MathUtils.constrainDouble(power, -1, 0);
            potentialReason = ClimbRestrictionReason.TooCloseToWinch;
        }
        
        climbRestrictionProp.set(potentialReason.toString());
        motor.simpleSet(power);
        motor.updateTelemetryProperties();
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
