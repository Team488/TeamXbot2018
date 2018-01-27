package competition.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ElevatorSubsystem extends BaseSubsystem {

    double defaultElevatorPower;
    CommonLibFactory clf;
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

    public XCANTalon motor;
    public XDigitalInput calibrationSensor;

    @Inject
    public ElevatorSubsystem(CommonLibFactory clf, XPropertyManager propMan) {
        this.clf = clf;
        elevatorPower = propMan.createPersistentProperty("ElevatorPower", 0.4);
        elevatorTicksPerInch = propMan.createPersistentProperty("ElevatorTicksPerInch", 100);
        calibrationPower = propMan.createPersistentProperty("ElevatorCalibrationPower", 0.2);
        maxHeightInInches = propMan.createPersistentProperty("Elevator Max HeightInInches", 80);
        minHeightInInches = propMan.createPersistentProperty("Elevator Min HeightInInches", 3);

        calibrationOffset = 0;

        calibrationLatch = new Latch(false, EdgeType.RisingEdge, edge -> {
            if (edge == EdgeType.RisingEdge) {
                calibrate();
            }
        });
    }

    public void temporaryHack() {
        motor = clf.createCANTalon(40);
        motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        calibrationSensor = clf.createDigitalInput(1);
    }

    private void calibrate() {
        calibrationOffset = motor.getSelectedSensorPosition(0);
        isCalibrated = true;
    }

    /**
     * Directly sets the % power on the elevator motor. If the elevator is uncalibrated, power will be constrained.
     * 
     * @param power power percentage in robot scale
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

    public double currentHeight() {
        return ticksToInches(motor.getSelectedSensorPosition(0));
    }

    private double ticksToInches(double ticks) {
        double tpi = elevatorTicksPerInch.get();
        if (tpi == 0) {
            return 0;
        }

        return ((ticks - calibrationOffset) / tpi) + minHeightInInches.get();
    }

    public boolean isCalibrated() {
        return isCalibrated;
    }

    /**
     * Returns true if the elevator is close to its maximum height.
     */

    boolean isCloseToMaxmumHeight() {

        if (currentHeight() >= maxHeightInInches.get() * 0.9) {
            return true;
        }

        return false;

    }

    /**
     * Returns true if the elevator is close to its minimum height.
     */

    boolean isCloseToMinimumHeight() {

        if (currentHeight() < maxHeightInInches.get() * 0.15) {
            return true;
        }

        return false;
    }
}
