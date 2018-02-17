package competition.subsystems.wrist;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class WristSubsystem extends BaseSubsystem implements PeriodicDataSource {
    final DoubleProperty defaultWristPower;
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;

    public XCANTalon motor;
    
    final DoubleProperty currentWristAngleProp;
    final DoubleProperty wristTicksPerDegreeProp;
    final BooleanProperty wristCalibratedProp;
    final DoubleProperty wristUncalibratedPowerProp;
    
    int lowerLimit;
    int upperLimit;
    boolean calibrated = false;

    @Inject
    WristSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        defaultWristPower = propMan.createPersistentProperty("Wrist Default Power", 1);
        
        currentWristAngleProp = propMan.createEphemeralProperty("Wrist Current Angle", 0.0);
        wristTicksPerDegreeProp = propMan.createPersistentProperty("Wrist ticks per degree", 1);
        wristCalibratedProp = propMan.createEphemeralProperty("Wrist Calibrated", false);
        wristUncalibratedPowerProp = propMan.createPersistentProperty("Wrist calibration power", 0.3);
        
        if (contract.wristReady()) {
            initializeMotor();
        }
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

    private void initializeMotor() {
        motor = clf.createCANTalon(contract.getWristMaster().channel);
        motor.setInverted(contract.getWristMaster().inverted);
        motor.createTelemetryProperties("Wrist");
        
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        motor.setSensorPhase(contract.getWristEncoder().inverted);
        
        motor.setNeutralMode(NeutralMode.Brake);
    }
    
    public void uncalibrate() {
        calibrated = false;
        
        setSoftLimitsEnabled(false);
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
        int tickRange = (int)(contract.getWristMaximumAngle() * wristTicksPerDegreeProp.get());
        lowerLimit = highestPosition - tickRange;
        
        log.info("Lower limit set at: " + lowerLimit);
        motor.configReverseSoftLimitThreshold(lowerLimit, 0);
    }
    
    public void calibrateHere() {
        calibrateAt(motor.getSelectedSensorPosition(0));
    }
    
    /**
     * Directly set the power of the Wrist.
     * @param power Negative to move down, positive to move up.
     */
    public void setPower(double power) {
        if (!calibrated) {
            power = MathUtils.constrainDouble(
                    power, 
                    -wristUncalibratedPowerProp.get(), 
                    wristUncalibratedPowerProp.get());
        }        
        
        motor.simpleSet(power);
    }

    /**
     * angles the Gripper up
     */
    public void goUp() {
        motor.simpleSet(defaultWristPower.get());
    }

    /**
     * angles the Gripper down
     */
    public void goDown() {
        motor.simpleSet(-defaultWristPower.get());
    }

    /**
     * stops the Gripper
     */
    public void stop() {
        motor.simpleSet(0);
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
    }
}
