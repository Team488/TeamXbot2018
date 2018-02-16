package competition.subsystems.elevator;

import java.util.function.Supplier;

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
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ElevatorSubsystem extends BaseSetpointSubsystem implements PeriodicDataSource {

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
    final DoubleProperty maxHeightInInches;
    final DoubleProperty minHeightInInches;
    final DoubleProperty elevatorTargetHeight;
    final DoubleProperty currentTicks;
    final DoubleProperty currentHeight;
    final BooleanProperty lowerLimitProp;
    final BooleanProperty upperLimitProp;
    private final DoubleProperty targetScaleHighHeight;
    private final DoubleProperty targetScaleMidHeight;
    private final DoubleProperty targetSwitchDropHeight;
    private final DoubleProperty targetPickUpHeight;
    final DoubleProperty elevatorPeakCurrentLimit;
    final DoubleProperty elevatorPeakCurrentDuration;
    final DoubleProperty elevatorContinuousCurrentLimit;

    public XCANTalon motor;
    public XDigitalInput lowerLimitSwitch;
    public XDigitalInput upperLimitSwitch;
    
    int updateMotorValuesCounter = 0;

    @Inject
    public ElevatorSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        heightNearLowLimit = propMan.createPersistentProperty("Elevator Height Near Low Limit", 13);
        heightNearHighLimit = propMan.createPersistentProperty("Elevator Height Near High Limit", 70);
        powerNearLowLimit = propMan.createPersistentProperty("Max Power Near Low Limit", -0.3);
        powerNearHighLimit = propMan.createPersistentProperty("Max Power Near High Limit", 0.3);
        elevatorPower = propMan.createPersistentProperty("ElevatorPower", 0.4);
        elevatorTicksPerInch = propMan.createPersistentProperty("ElevatorTicksPerInch", 100);
        calibrationPower = propMan.createPersistentProperty("ElevatorCalibrationPower", 0.2);
        maxHeightInInches = propMan.createPersistentProperty("Elevator Max HeightInInches", 80);
        minHeightInInches = propMan.createPersistentProperty("Elevator Min HeightInInches", 3);
        elevatorTargetHeight = propMan.createEphemeralProperty("targetHeight", maxHeightInInches.get());
        currentTicks = propMan.createEphemeralProperty("Elevator current ticks", 0.0);
        currentHeight = propMan.createEphemeralProperty("Elevator current height", 0.0);
        lowerLimitProp = propMan.createEphemeralProperty("Elevator Lower Limit", false);
        upperLimitProp = propMan.createEphemeralProperty("Elevator Upper Limit", false);
        targetScaleHighHeight = propMan.createPersistentProperty("Elevator scale high", 76.5);
        targetScaleMidHeight = propMan.createPersistentProperty("Elevator scale mid", 64.5);
        targetSwitchDropHeight = propMan.createPersistentProperty("Elevator switch drop height", 19.0);
        targetPickUpHeight = propMan.createPersistentProperty("Elevator pickup height", 3.0);
        elevatorPeakCurrentLimit = propMan.createPersistentProperty("Elevator peak current limit", 35);
        elevatorPeakCurrentDuration = propMan.createPersistentProperty("Elevator peak current duration", 200);
        elevatorContinuousCurrentLimit = propMan.createPersistentProperty("Elevator continuous current limit", 30);

        calibrationOffset = 0.0;

        calibrationLatch = new Latch(false, EdgeType.RisingEdge, edge -> {
            if (edge == EdgeType.RisingEdge) {
                calibrateHere();
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
        motor.setSensorPhase(true);

        motor.configPeakCurrentLimit((int) elevatorPeakCurrentLimit.get(), 0);
        motor.configPeakCurrentDuration((int) elevatorPeakCurrentDuration.get(), 0);
        motor.configContinuousCurrentLimit((int) elevatorContinuousCurrentLimit.get(), 0);
        motor.enableCurrentLimit(true);

        motor.createTelemetryProperties("ElevatorMotor");
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

        if (contract.elevatorLowerLimitReady()) {
            boolean sensorHit = lowerLimitSupplier.get();
            calibrationLatch.setValue(sensorHit);

            // If the lower-bound sensor is hit, then we need to prevent the mechanism from
            // lowering any further.
            if (sensorHit) {
                power = MathUtils.constrainDouble(power, 0, 1);
            }
        }

        if (contract.elevatorUpperLimitReady()) {
            boolean sensorHit = upperLimitSupplier.get();

            // If the upper-bound sensor is hit, then we need to prevent the mechanism from rising any further.
            if (sensorHit) {
                power = MathUtils.constrainDouble(power, -1, 0);
            }
        }

        // If the elevator is not calibrated, then maximum power should be constrained.
        if (!isCalibrated) {
            power = MathUtils.constrainDouble(power, -calibrationPower.get(), calibrationPower.get());
        }

        if (isCalibrated) {
            // if we are above the max, only go down.
            double currentHeight = getCurrentHeightInInches();
            if (currentHeight > getMaxHeightInInches()) {
                power = MathUtils.constrainDouble(power, -1, 0);
            }
            // if we are below the min, can only go up.
            if (currentHeight < getMinHeightInInches()) {
                power = MathUtils.constrainDouble(power, 0, 1);
            }
            if(getCurrentHeightInInches() < getHeightNearLowLimit()) {
                power = MathUtils.constrainDouble(power, getPowerNearLowLimit(), 1);
            }
            if(getCurrentHeightInInches() > getHeightNearHighLimit()) {
                power = MathUtils.constrainDouble(power, -1, getPowerNearHighLimit());
            }
        }

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

    public void setTargetHeight(double height) {
        elevatorTargetHeight.set(height);
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
        }

        if (contract.elevatorLowerLimitReady()) {
            lowerLimitProp.set(lowerLimitSwitch.get());
        }

        if (contract.elevatorUpperLimitReady()) {
            upperLimitProp.set(upperLimitSwitch.get());
        }
        
        updateMotorValuesCounter++;
        
        // roughly 5 seconds at 30 Hz
        if (updateMotorValuesCounter == 150 ) {
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
}
