package competition.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
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

    final DoubleProperty maxHeightInInches;
    final DoubleProperty minHeightInInches;
    final DoubleProperty elevatorTargetHeight;
    final DoubleProperty currentTicks;
    final DoubleProperty currentHeight;

    public XCANTalon motor;
    public XDigitalInput calibrationSensor;

    @Inject
    public ElevatorSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        elevatorPower = propMan.createPersistentProperty("ElevatorPower", 0.4);
        elevatorTicksPerInch = propMan.createPersistentProperty("ElevatorTicksPerInch", 100);
        calibrationPower = propMan.createPersistentProperty("ElevatorCalibrationPower", 0.2);
        maxHeightInInches = propMan.createPersistentProperty("Elevator Max HeightInInches", 80);
        minHeightInInches = propMan.createPersistentProperty("Elevator Min HeightInInches", 3);
        elevatorTargetHeight = propMan.createEphemeralProperty("targetHeight", maxHeightInInches.get());
        currentTicks = propMan.createEphemeralProperty("Elevator current ticks", 0.0);
        currentHeight = propMan.createEphemeralProperty("Elevator current height", 0.0);
        calibrationOffset = 0.0;

        calibrationLatch = new Latch(false, EdgeType.RisingEdge, edge -> {
            if (edge == EdgeType.RisingEdge) {
                calibrate();
            }
        });

        if (contract.elevatorReady()) {
            temporaryHack();
        }

    }

    public void temporaryHack() {
        motor = clf.createCANTalon(contract.getElevatorMaster().channel);
        motor.setInverted(contract.getElevatorMaster().inverted);
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        calibrationSensor = clf.createDigitalInput(1);
    }

    private void calibrate() {
        calibrationOffset = motor.getSelectedSensorPosition(0);
        isCalibrated = true;
    }

    /**
     * 
     * @param forceCalibrated - when true, the calibrate method is run and is
     * calibrated is set to true. when false, is calibrated is set to false
     */
    public void setCalibrate(boolean forceCalibrated) {
        if (forceCalibrated) {
            calibrate();
        } else {
            isCalibrated = false;
        }
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
        boolean sensorHit = calibrationSensor.get();
        calibrationLatch.setValue(sensorHit);

        // If the lower-bound sensor is hit, then we need to prevent the mechanism from lowering any further.
        if (sensorHit) {
            power = MathUtils.constrainDouble(power, 0, 1);
        }

        // If the elevator is not calibrated, then maximum power should be constrained.
        if (!isCalibrated) {
            power = MathUtils.constrainDouble(power, -calibrationPower.get(), calibrationPower.get());
        }

        motor.simpleSet(power);
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

    public double getCurrentTick() {
        return ticksToInches(motor.getSelectedSensorPosition(0));
    }

    public double minHeightInInches() {
        return ticksToInches(minHeightInInches.get());
    }

    public double maxHeightInInches() {
        return ticksToInches(maxHeightInInches.get());
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
        currentTicks.set(getCurrentTick());
        currentHeight.set(getCurrentHeightInInches());
    }
}
