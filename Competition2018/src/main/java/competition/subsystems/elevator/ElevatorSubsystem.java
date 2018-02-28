package competition.subsystems.elevator;

import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSetpointSubsystem;
import competition.ElectricalContract2018;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.PIDPropertyManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ElevatorSubsystem extends BaseSetpointSubsystem implements PeriodicDataSource {

    public enum ElevatorPowerRestrictionReason {
        FullPowerAvailable, LowerLimitSwitch, UpperLimitSwitch, Uncalibrated, AboveMaxHeight, NearMaxHeight, BelowMinHeight, NearMinHeight,
    }

    public enum ElevatorGoal {
        PickUpHeight, Switch, ScaleLow, ScaleMid, ScaleHigh
    }

    final StringProperty elevatorRestrictionReasonProp;

    double defaultElevatorPower;
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;
    final DoubleProperty elevatorPower;
    final DoubleProperty elevatorTicksPerInch;

    /**
     * If our elevator is uncalibrated, we don't allow large power inputs.
     */
    final DoubleProperty calibrationPower;

    private boolean isCalibrated;
    private double calibrationOffset;
    private final Latch calibrationLatch;

    private Supplier<Boolean> lowerLimitSupplier;
    private Supplier<Boolean> upperLimitSupplier;

    final DoubleProperty powerNearLowLimit;
    final DoubleProperty powerNearHighLimit;
    final DoubleProperty heightNearHighLimit;
    final DoubleProperty heightNearLowLimit;
    final DoubleProperty talonMaxVelocity;
    final DoubleProperty talonMaxAcceleration;
    final DoubleProperty maxHeightInInches;
    final DoubleProperty minHeightInInches;
    final DoubleProperty elevatorTargetHeight;
    final DoubleProperty currentTicks;
    final DoubleProperty currentHeight;
    final DoubleProperty currentVelocity;
    final BooleanProperty lowerLimitProp;
    final BooleanProperty upperLimitProp;
    final BooleanProperty calibratedProp;
    private final DoubleProperty targetScaleHighHeight;
    private final DoubleProperty targetScaleMidHeight;
    private final DoubleProperty targetScaleLowHeight;
    private final DoubleProperty targetSwitchDropHeight;
    private final DoubleProperty targetPickUpHeight;
    final DoubleProperty elevatorPeakCurrentLimit;
    final DoubleProperty elevatorPeakCurrentDuration;
    final DoubleProperty elevatorContinuousCurrentLimit;
    final PIDPropertyManager motionMagicProperties;
    public XCANTalon motor;
    public XDigitalInput lowerLimitSwitch;
    public XDigitalInput upperLimitSwitch;
    final PIDManager positionalPid;
    final PIDManager velocityPid;

    int updateMotorValuesCounter = 0;

    final Latch motionMagicLatch;

    @Inject
    public ElevatorSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract,
            PIDFactory pf) {
        this.clf = clf;
        this.contract = contract;
        elevatorPower = propMan.createPersistentProperty(getPrefix() + "Standard Power", 0.4);
        elevatorTicksPerInch = propMan.createPersistentProperty(getPrefix() + "TicksPerInch", 100);
        calibrationPower = propMan.createPersistentProperty(getPrefix() + "CalibrationPower", 0.3);
        maxHeightInInches = propMan.createPersistentProperty(getPrefix() + "Max HeightInInches", 80);
        minHeightInInches = propMan.createPersistentProperty(getPrefix() + "Min HeightInInches", 3);
        elevatorTargetHeight = propMan.createEphemeralProperty(getPrefix() + "TargetHeight", maxHeightInInches.get());
        currentTicks = propMan.createEphemeralProperty(getPrefix() + "Current ticks", 0.0);
        currentHeight = propMan.createEphemeralProperty(getPrefix() + "Current height", 0.0);
        lowerLimitProp = propMan.createEphemeralProperty(getPrefix() + "Lower Limit", false);
        upperLimitProp = propMan.createEphemeralProperty(getPrefix() + "Upper Limit", false);
        targetScaleHighHeight = propMan.createPersistentProperty(getPrefix() + "Scale high", 76.5);
        targetScaleMidHeight = propMan.createPersistentProperty(getPrefix() + "Scale mid", 64.5);
        targetScaleLowHeight = propMan.createPersistentProperty(getPrefix() + "Scale low", 60.0);
        targetSwitchDropHeight = propMan.createPersistentProperty(getPrefix() + "Switch drop height", 35);
        targetPickUpHeight = propMan.createPersistentProperty(getPrefix() + "Pickup height", 3.0);
        elevatorPeakCurrentLimit = propMan.createPersistentProperty(getPrefix() + "Peak current limit", 40);
        elevatorPeakCurrentDuration = propMan.createPersistentProperty(getPrefix() + "Peak current duration", 3000);
        elevatorContinuousCurrentLimit = propMan.createPersistentProperty(getPrefix() + "Continuous current limit", 30);
        motionMagicProperties = pf.createPIDPropertyManager(getPrefix() + "Motion Magic", 0.3, 0, 0, 0.688);
        talonMaxVelocity = propMan.createPersistentProperty(getPrefix() + "Max Velocity", 1400);
        talonMaxAcceleration = propMan.createPersistentProperty(getPrefix() + "Max Accleration", 1400);
        elevatorRestrictionReasonProp = propMan.createEphemeralProperty(getPrefix() + "Restriction Reason",
                "Waiting to run...");
        calibratedProp = propMan.createEphemeralProperty(getPrefix() + "Calibrated", false);
        heightNearLowLimit = propMan.createPersistentProperty(getPrefix() + "Height Near Low Limit",
                minHeightInInches.get() + 10);
        heightNearHighLimit = propMan.createPersistentProperty(getPrefix() + "Height Near High Limit",
                maxHeightInInches.get() - 10);
        powerNearLowLimit = propMan.createPersistentProperty(getPrefix() + "Max Power Near Low Limit", 0.2);
        powerNearHighLimit = propMan.createPersistentProperty(getPrefix() + "Max Power Near High Limit", 0.5);
        positionalPid = pf.createPIDManager(getPrefix() + "Position", 0.1, 0, 0);
        velocityPid = pf.createPIDManager(getPrefix() + "Velocity", 0.004, 0, 0);
        currentVelocity = propMan.createEphemeralProperty(getPrefix() + "Current Velocity", 0);
        calibrationOffset = 0.0;

        calibrationLatch = new Latch(false, EdgeType.RisingEdge, edge -> {
            if (edge == EdgeType.RisingEdge) {
                calibrateHere();
            }
        });

        motionMagicLatch = new Latch(false, EdgeType.RisingEdge, edge -> {
            if (edge == EdgeType.RisingEdge) {
                configureMotionMagic();
            }
        });

        if (contract.elevatorReady()) {
            initializeMotor();
        }

        if (contract.elevatorLowerLimitReady()) {
            initializeLowerLimit();
        }

        if (contract.elevatorUpperLimitReady()) {
            initializeUpperLimit();
        }

        if (contract.elevatorUsesTalonLimits()) {
            initializeTalonLimits();
        }
    }

    private void initializeMotor() {
        motor = clf.createCANTalon(contract.getElevatorMaster().channel);
        motor.setInverted(contract.getElevatorMaster().inverted);
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        motor.setSensorPhase(contract.getElevatorEncoder().inverted);

        motor.configPeakCurrentLimit((int) elevatorPeakCurrentLimit.get(), 0);
        motor.configPeakCurrentDuration((int) elevatorPeakCurrentDuration.get(), 0);
        motor.configContinuousCurrentLimit((int) elevatorContinuousCurrentLimit.get(), 0);
        motor.enableCurrentLimit(true);

        motor.configPeakOutputReverse(-0.2, 0);
        motor.configNominalOutputForward(0.3, 0);

        motor.createTelemetryProperties(getPrefix(), "Motor");
    }

    private void setRestrictionReason(ElevatorPowerRestrictionReason reason) {
        elevatorRestrictionReasonProp.set(reason.toString());
    }

    public void enableCurrentLimit() {
        motor.enableCurrentLimit(true);
    }

    public void disableCurrentLimit() {
        motor.enableCurrentLimit(false);
    }

    private void initializeLowerLimit() {
        lowerLimitSwitch = clf.createDigitalInput(contract.getElevatorLowerLimit().channel);
        lowerLimitSwitch.setInverted(contract.getElevatorLowerLimit().inverted);
        lowerLimitSupplier = () -> lowerLimitSwitch.get();
    }

    private void initializeUpperLimit() {
        upperLimitSwitch = clf.createDigitalInput(contract.getElevatorUpperLimit().channel);
        upperLimitSwitch.setInverted(contract.getElevatorUpperLimit().inverted);
        upperLimitSupplier = () -> upperLimitSwitch.get();
    }

    private void initializeTalonLimits() {
        // Upper limit
        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

        // Lower limit
        motor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

        upperLimitSupplier = () -> motor.isFwdLimitSwitchClosed();
        lowerLimitSupplier = () -> motor.isRevLimitSwitchClosed();
    }

    public void calibrateHere() {
        calibrateAt(motor.getSelectedSensorPosition(0));
    }

    public void calibrateAt(int lowestPosition) {
        log.info("Calibrating elevator with lowest position of " + lowestPosition);
        calibrationOffset = lowestPosition;
        isCalibrated = true;

        motor.configReverseSoftLimitThreshold(lowestPosition, 0);

        // calculate the upper limit and set safeties.
        double inchRange = getMaxHeightInInches() - getMinHeightInInches();
        int tickRange = (int) (elevatorTicksPerInch.get() * inchRange);
        int upperLimit = tickRange + lowestPosition;

        log.info("Upper limit set at: " + upperLimit);
        motor.configForwardSoftLimitThreshold(upperLimit, 0);

        setSoftLimitsEnabled(true);

        setTargetHeight(getCurrentHeightInInches());
    }

    public void uncalibrate() {
        isCalibrated = false;
        setSoftLimitsEnabled(false);
    }

    private void setSoftLimitsEnabled(boolean on) {
        motor.configReverseSoftLimitEnable(on, 0);
        motor.configForwardSoftLimitEnable(on, 0);
    }

    public boolean isCalibrated() {
        return isCalibrated;
    }

    /**
     * Directly sets the % power on the elevator motor. If the elevator is uncalibrated, power will be constrained.
     * 
     * @param power
     *            power percentage in robot scale
     */
    public void setPower(double power) {

        motionMagicLatch.setValue(false);
        ElevatorPowerRestrictionReason reason = ElevatorPowerRestrictionReason.FullPowerAvailable;

        if (contract.elevatorLowerLimitReady()) {
            boolean sensorHit = lowerLimitSupplier.get();
            calibrationLatch.setValue(sensorHit);

            // If the lower-bound sensor is hit, then we need to prevent the mechanism from
            // lowering any further.
            if (sensorHit) {
                power = MathUtils.constrainDouble(power, 0, 1);
                reason = ElevatorPowerRestrictionReason.LowerLimitSwitch;
            }
        }

        if (contract.elevatorUpperLimitReady()) {

            boolean sensorHit = upperLimitSupplier.get();

            // If the upper-bound sensor is hit, then we need to prevent the mechanism from rising any further.
            if (sensorHit) {
                power = MathUtils.constrainDouble(power, -1, 0);
                reason = ElevatorPowerRestrictionReason.UpperLimitSwitch;
            }
        }

        // If the elevator is not calibrated, then maximum power should be constrained.
        if (!isCalibrated) {
            power = MathUtils.constrainDouble(power, -calibrationPower.get(), calibrationPower.get());
            reason = ElevatorPowerRestrictionReason.Uncalibrated;
        }

        if (isCalibrated) {
            // if we are above the max, only go down.
            double currentHeight = getCurrentHeightInInches();
            if (currentHeight > getMaxHeightInInches()) {
                power = MathUtils.constrainDouble(power, -1, 0);
                reason = ElevatorPowerRestrictionReason.AboveMaxHeight;
            }
            // if we are below the min, can only go up.
            if (currentHeight < getMinHeightInInches()) {
                power = MathUtils.constrainDouble(power, 0, 1);
                reason = ElevatorPowerRestrictionReason.BelowMinHeight;
            }
            if (getCurrentHeightInInches() < getHeightNearLowLimit()
                    && getCurrentHeightInInches() > getMinHeightInInches()) {
                power = MathUtils.constrainDouble(power, -getPowerNearLowLimit(), 1);
                reason = ElevatorPowerRestrictionReason.NearMinHeight;
            }
            if (getCurrentHeightInInches() > getHeightNearHighLimit()
                    && getCurrentHeightInInches() < getMaxHeightInInches()) {
                power = MathUtils.constrainDouble(power, -1, getPowerNearHighLimit());
                reason = ElevatorPowerRestrictionReason.NearMaxHeight;
            }
        }

        motor.simpleSet(power);
        setRestrictionReason(reason);
    }

    public void insanelyDangerousSetPower(double power) {
        motor.simpleSet(power);
    }

    public double getPowerNearLowLimit() {
        return powerNearLowLimit.get();
    }

    public double getPowerNearHighLimit() {
        return powerNearHighLimit.get();
    }

    public double getHeightNearLowLimit() {
        return heightNearLowLimit.get();
    }

    public double getHeightNearHighLimit() {
        return heightNearHighLimit.get();
    }

    public void configureMotionMagic() {
        motor.configMotionCruiseVelocity((int) talonMaxVelocity.get(), 0);
        motor.configMotionAcceleration((int) talonMaxAcceleration.get(), 0);

        motor.config_kP(0, this.motionMagicProperties.getP(), 0);
        motor.config_kI(0, this.motionMagicProperties.getI(), 0);
        motor.config_kD(0, this.motionMagicProperties.getD(), 0);
        motor.config_kF(0, this.motionMagicProperties.getF(), 0);
    }

    public void motionMagicToHeight(double heightInInches) {
        motionMagicLatch.setValue(true);

        if (isCalibrated) {
            double targetTicks = inchesToTicks(heightInInches);

            motor.set(ControlMode.MotionMagic, targetTicks);
        }
    }

    public void setTargetHeight(double height) {
        elevatorTargetHeight.set(height);
    }

    public void setTargetHeight(ElevatorGoal elevatorGoal) {
        switch (elevatorGoal) {
        case PickUpHeight:
            setTargetHeight(targetPickUpHeight.get());
            break;
        case Switch:
            setTargetHeight(targetSwitchDropHeight.get());
            break;
        case ScaleLow:
            setTargetHeight(targetScaleLowHeight.get());
            break;
        case ScaleMid:
            setTargetHeight(targetScaleMidHeight.get());
            break;
        case ScaleHigh:
            setTargetHeight(targetScaleHighHeight.get());
            break;
        default:
            log.error(elevatorGoal + ": is an invalid type");
            break;
        }
    }

    public double getTargetHeight() {
        return elevatorTargetHeight.get();
    }

    /**
     * Raises the elevator. Power is controlled by a property.
     */
    public void rise() {
        setPower(elevatorPower.get());
    }

    /**
     * Lower the elevator. Power is controlled by a property.
     */
    public void lower() {
        setPower(-elevatorPower.get());
    }

    public void stop() {
        setPower(0);
    }

    public double getCurrentHeightInInches() {
        return ticksToInches(motor.getSelectedSensorPosition(0));
    }

    public double getVelocityInchesPerSecond() {
        return motor.getSelectedSensorVelocity(0) * 10 / elevatorTicksPerInch.get();
    }

    public int getCurrentTick() {
        return motor.getSelectedSensorPosition(0);
    }

    public double getMinHeightInInches() {
        return minHeightInInches.get();
    }

    public double getMaxHeightInInches() {
        return maxHeightInInches.get();
    }

    public void setTickPerInch(double ticksPerInch) {
        elevatorTicksPerInch.set(ticksPerInch);
    }

    private double ticksToInches(double ticks) {
        double tpi = elevatorTicksPerInch.get();
        if (tpi == 0) {
            return 0;
        }

        return ((ticks - calibrationOffset) / tpi) + minHeightInInches.get();
    }

    private double inchesToTicks(double inches) {
        return (inches - minHeightInInches.get()) * elevatorTicksPerInch.get() + calibrationOffset;
    }

    public double getMaxHeight() {
        return maxHeightInInches.get();
    }

    public double getMinHeight() {
        return minHeightInInches.get();
    }

    @Override
    public void updatePeriodicData() {
        if (contract.elevatorReady()) {
            currentTicks.set(getCurrentTick());
            currentHeight.set(getCurrentHeightInInches());
            motor.updateTelemetryProperties();
            currentVelocity.set(getVelocityInchesPerSecond());
        }
        if (contract.elevatorLowerLimitReady()) {
            lowerLimitProp.set(lowerLimitSwitch.get());
        }

        if (contract.elevatorUpperLimitReady()) {
            upperLimitProp.set(upperLimitSwitch.get());
        }

        updateMotorValuesCounter++;
        calibratedProp.set(isCalibrated);

        // roughly 5 seconds at 30 Hz
        if (updateMotorValuesCounter == 150) {
            updateMotorValuesCounter = 0;
            motor.configPeakCurrentLimit((int) elevatorPeakCurrentLimit.get(), 0);
            motor.configPeakCurrentDuration((int) elevatorPeakCurrentDuration.get(), 0);
            motor.configContinuousCurrentLimit((int) elevatorContinuousCurrentLimit.get(), 0);
        }
    }

    public double getTargetScaleHighHeight() {
        return targetScaleHighHeight.get();
    }

    public double getTargetScaleMidHeight() {
        return targetScaleMidHeight.get();
    }

    public double getTargetSwitchDropHeight() {
        return targetSwitchDropHeight.get();
    }

    public double getTargetPickUpHeight() {
        return targetPickUpHeight.get();

    }

    public double getLowerLimitInTicks() {
        return calibrationOffset;
    }

    public PIDManager getPositionalPid() {
        return positionalPid;
    }

    public PIDManager getVelocityPid() {
        return velocityPid;
    }
}
