package competition.subsystems.wrist;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
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
        ((MockCANTalon)wrist.motor).setPosition(10);
        wrist.calibrateHere();
        
        assertEquals(10, wrist.getUpperLimitInTicks(), 0.001);
        assertEquals(
                wrist.getUpperLimitInTicks() - (contract.getWristMaximumAngle()*wrist.getWristTicksPerDegree()), 
                wrist.getLowerLimitInTicks(), 
                0.001);
    }
    
    private void verifyWristPower(double expectedPower) {
        assertEquals(expectedPower, wrist.motor.getMotorOutputPercent(), 0.001);
    }
}
