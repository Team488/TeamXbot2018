package competition.subsystems.wrist;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class WristSubsystem extends BaseSetpointSubsystem implements PeriodicDataSource {
    
    public enum WristPowerRestrictionReason {
        FullPowerAvailable, LowerLimitSwitch, UpperLimitSwitch, Uncalibrated, IsWithinSafetyZone
    }
    
    final StringProperty wristRestrictionReasonProp;
    final DoubleProperty maximumWristPower;
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;
    private final Latch calibrationLatch;

    public XCANTalon motor;
    public XDigitalInput lowerLimitSwitch;
    public XDigitalInput upperLimitSwitch;

    final DoubleProperty currentWristAngleProp;
    final DoubleProperty wristTicksPerDegreeProp;
    final BooleanProperty wristCalibratedProp;
    final DoubleProperty wristUncalibratedPowerProp;
    final DoubleProperty targetAngle;
    final BooleanProperty lowerLimitProp;
    final BooleanProperty upperLimitProp;

    private final BooleanProperty safetyZoneEnabled;
    private final DoubleProperty safetyZoneMaxAngle;
    private final DoubleProperty safetyZoneStartHeight;

    int lowerLimit;
    int upperLimit;
    boolean calibrated = false;

    public enum WristPosition {
        Down, Middle, Up
    }
    
    @Inject
    WristSubsystem(CommonLibFactory clf, XPropertyManager propMan,
            ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        maximumWristPower = propMan.createPersistentProperty(getPrefix() + "Maximum Power", 0.3);

        wristRestrictionReasonProp = propMan.createEphemeralProperty(getPrefix() + "Restriction Reason","Waiting to run...");
        currentWristAngleProp = propMan.createEphemeralProperty(getPrefix() + "Current Angle", 0.0);
        wristTicksPerDegreeProp = propMan.createPersistentProperty(getPrefix() + "Ticks per degree", 1);
        wristCalibratedProp = propMan.createEphemeralProperty(getPrefix() + "Calibrated", false);
        wristUncalibratedPowerProp = propMan.createPersistentProperty(getPrefix() + "Calibration power", 0.3);
        targetAngle = propMan.createEphemeralProperty(getPrefix() + "Target Angle", contract.getWristMaximumAngle());
        lowerLimitProp = propMan.createEphemeralProperty(getPrefix() + "Lower Limit", false);
        upperLimitProp = propMan.createEphemeralProperty(getPrefix() + "Upper Limit", false);
        safetyZoneEnabled = propMan.createPersistentProperty(getPrefix() + "Safety zone/Is enabled", true);
        safetyZoneMaxAngle = propMan.createPersistentProperty(getPrefix() + "Safety zone/Max angle", 50);
        safetyZoneStartHeight = propMan.createPersistentProperty(getPrefix() + "Safety zone/Start height (in)", 40);

        if (contract.wristReady()) {
            initializeComponents();
        }

        calibrationLatch = new Latch(false, EdgeType.RisingEdge, edge -> {
            if (edge == EdgeType.RisingEdge) {
                calibrateHere();
            }
        });
    }

    public double getMaximumAllowedPower() {
        return maximumWristPower.get();
    }

    public void setTargetAngle(double angle) {
        angle = modifyAngleForSafeties(angle);
        targetAngle.set(angle);
    }

    public void setTargetAngle(WristPosition position) {
        if (position == WristPosition.Down) {
            setTargetAngle(lowerLimit);
        } else if (position == WristPosition.Up) {
            setTargetAngle(upperLimit);
        } else {
            log.error("Wrist Position: " + position + " is an invalid postion");
        }
    }
    
    public double getTargetAngle() {
        return targetAngle.get();
    }

    public double getUncalibratedPowerFactor() {
        return wristUncalibratedPowerProp.get();
    }

    public double getWristTicksPerDegree() {
        return wristTicksPerDegreeProp.get();
    }

    public int getLowerLimitInTicks() {
        return lowerLimit;
    }

    public int getUpperLimitInTicks() {
        return upperLimit;
    }

    private void initializeComponents() {
        motor = clf.createCANTalon(contract.getWristMaster().channel);
        motor.setInverted(contract.getWristMaster().inverted);
        motor.createTelemetryProperties(getPrefix(), "Motor");

        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        motor.setSensorPhase(contract.getWristEncoder().inverted);

        motor.setNeutralMode(NeutralMode.Brake);

        motor.configNominalOutputForward(0, 0);
        motor.configNominalOutputReverse(0, 0);

        uncalibrate();

        if (contract.isWristLimitsReady()) {
            upperLimitSwitch = clf.createDigitalInput(contract.getWristUpperLimit().channel);
            upperLimitSwitch.setInverted(contract.getWristUpperLimit().inverted);

            lowerLimitSwitch = clf.createDigitalInput(contract.getWristLowerLimit().channel);
            lowerLimitSwitch.setInverted(contract.getWristLowerLimit().inverted);
        } else {
            // We don't have any limit switches, so we have to assume we started vertically.
            calibrateHere();
        }
    }

    public void uncalibrate() {
        calibrated = false;

        setSoftLimitsEnabled(false);
    }
    
    private void setRestrictionReason(WristPowerRestrictionReason reason) {
        wristRestrictionReasonProp.set(reason.toString());
    }

    private void setSoftLimitsEnabled(boolean on) {
        motor.configReverseSoftLimitEnable(on, 0);
        motor.configForwardSoftLimitEnable(on, 0);
    }

    public boolean getIsCalibrated() {
        return calibrated;
    }

    public void calibrateAt(int highestPosition) {
        log.info("Calibrating wrist at " + highestPosition);
        upperLimit = highestPosition;
        calibrated = true;

        motor.configForwardSoftLimitThreshold(upperLimit, 0);
        setSoftLimitsEnabled(true);

        // calculate lower limit and set safeties
        int tickRange = (int) (contract.getWristMaximumAngle() * wristTicksPerDegreeProp.get());
        lowerLimit = highestPosition - tickRange;

        log.info("Lower limit set at: " + lowerLimit);
        motor.configReverseSoftLimitThreshold(lowerLimit, 0);

        setTargetAngle(getTargetAngle());
    }

    public double getWristAngle() {
        if (getWristTicksPerDegree() == 0) {
            return 0;
        }
        return ((motor.getSelectedSensorPosition(0) - upperLimit) / getWristTicksPerDegree())
                + contract.getWristMaximumAngle();
    }

    public void calibrateHere() {
        calibrateAt(motor.getSelectedSensorPosition(0));
    }

    /**
     * Directly set the power of the Wrist.
     * 
     * @param power
     *            Negative to move down, positive to move up.
     */
    public void setPower(double power) {
        WristPowerRestrictionReason reason = WristPowerRestrictionReason.FullPowerAvailable;
        
        if (contract.isWristLimitsReady()) {
            calibrationLatch.setValue(upperLimitSwitch.get());

            if (upperLimitSwitch.get()) {
                power = MathUtils.constrainDouble(power, -1, 0);
                reason = WristPowerRestrictionReason.UpperLimitSwitch;
            }

            if (lowerLimitSwitch.get()) {
                power = MathUtils.constrainDouble(power, 0, 1);
                reason = WristPowerRestrictionReason.LowerLimitSwitch;
            }
        }

        if (!calibrated) {
            power = MathUtils.constrainDouble(power, -wristUncalibratedPowerProp.get(), wristUncalibratedPowerProp.get());
            reason = WristPowerRestrictionReason.Uncalibrated;
        } else {
            power = MathUtils.constrainDouble(power, -maximumWristPower.get(), maximumWristPower.get());
            reason = WristPowerRestrictionReason.FullPowerAvailable;
        }

        motor.simpleSet(power);
        setRestrictionReason(reason);
    }
    
    public void insanelyDangerousSetPower(double power) {
        setSoftLimitsEnabled(false);
        motor.simpleSet(power);
    }
    
    public double modifyAngleForSafeties(double angle) {
        return angle;
    }

    /**
     * angles the Gripper up
     */
    public void goUp() {
        setPower(maximumWristPower.get());
    }

    /**
     * angles the Gripper down
     */
    public void goDown() {
        setPower(-maximumWristPower.get());
    }

    /**
     * stops the Gripper
     */
    public void stop() {
        setPower(0);
    }

    /**
     * returns orientation of gripper
     */
    public double getDeployOrientation() {
        return motor.getSelectedSensorPosition(0);
    }

    @Override
    public void updatePeriodicData() {
        motor.updateTelemetryProperties();
        wristCalibratedProp.set(calibrated);
        currentWristAngleProp.set(getWristAngle());
        
        if (contract.isWristLimitsReady()) {
          lowerLimitProp.set(lowerLimitSwitch.get());
          upperLimitProp.set(upperLimitSwitch.get());
        }/*
        if (isWithinSafetyZone()) {
            double newTargetAngle = modifyAngleForSafeties(getTargetAngle());
            targetAngle.set(newTargetAngle);
        } */
    }
}
