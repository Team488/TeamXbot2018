package competition.subsystems.wrist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj.MockDigitalInput;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class WristSubsystemTest extends BaseCompetitionTest {

    WristSubsystem wrist;

    @Override
    public void setUp() {
        super.setUp();

        wrist = injector.getInstance(WristSubsystem.class);
    }

    @Test
    public void testSimpleMethods() {
        wrist.calibrateHere();
        verifyWristPower(0);
        wrist.goUp();
        verifyWristPower(wrist.getMaximumAllowedPower());
        wrist.goDown();
        verifyWristPower(-wrist.getMaximumAllowedPower());
        wrist.stop();
        verifyWristPower(0);
        wrist.setPower(0.129898);
        verifyWristPower(0.129898);
    }

    @Test
    public void testCalibrationPowers() {
        wrist.setPower(0.7);
        verifyWristPower(wrist.getUncalibratedPowerFactor());

        wrist.calibrateHere();
        wrist.setPower(0.7);
        verifyWristPower(wrist.getMaximumAllowedPower());
    }

    @Test
    public void testCalibrationPoints() {
        ((MockCANTalon) wrist.motor).setPosition(10);
        wrist.calibrateHere();

        assertEquals(10, wrist.getUpperLimitInTicks(), 0.001);
        assertEquals(wrist.getUpperLimitInTicks() - (contract.getWristMaximumAngle() * wrist.getWristTicksPerDegree()),
                wrist.getLowerLimitInTicks(), 0.001);
    }

    public void testCalibrationViaLimitSwitch() {
        wrist.uncalibrate();
        ((MockCANTalon) wrist.motor).setPosition(10);

        assertFalse("Wrist should not be calibrated now", wrist.getIsCalibrated());

        setLimits(false, true);

        assertTrue("Upper limit pressed", wrist.upperLimitSwitch.get());

        wrist.setPower(1);
        verifyWristPower(0);
        assertTrue("Wrist now calibrated", wrist.getIsCalibrated());
    }

    public void testRespectLimitSwitches() {
        setLimits(false, false);
        wrist.setPower(1);
        verifyWristPower(1);
        wrist.setPower(-1);
        verifyWristPower(-1);

        setLimits(false, true);
        wrist.setPower(1);
        verifyWristPower(0);
        wrist.setPower(-1);
        verifyWristPower(-1);

        setLimits(true, false);
        wrist.setPower(1);
        verifyWristPower(1);
        wrist.setPower(-1);
        verifyWristPower(0);

        setLimits(true, true);
        wrist.setPower(1);
        verifyWristPower(0);
        wrist.setPower(-1);
        verifyWristPower(0);
    }

    private void verifyWristPower(double expectedPower) {
        assertEquals(expectedPower, wrist.motor.getMotorOutputPercent(), 0.001);
    }

    private void setLimits(boolean lower, boolean upper) {
        ((MockDigitalInput) wrist.lowerLimitSwitch).setValue(lower);
        ((MockDigitalInput) wrist.upperLimitSwitch).setValue(upper);
    }
}
