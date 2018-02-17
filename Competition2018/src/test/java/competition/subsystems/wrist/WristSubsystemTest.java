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
        verifyWristPower(1);
        wrist.goDown();
        verifyWristPower(-1);
        wrist.stop();
        verifyWristPower(0);
        wrist.setPower(0.48743847);
        verifyWristPower(0.48743847);
    }
    
    @Test
    public void testCalibrationPowers() {
        wrist.setPower(0.7);
        verifyWristPower(wrist.getUncalibratedPowerFactor());
        
        wrist.calibrateHere();
        wrist.setPower(0.7);
        verifyWristPower(0.7);
    }
    
    @Test
    public void testCalibrationPoints() {
        ((MockCANTalon)wrist.motor).setPosition(0);
        wrist.calibrateHere();
        
        assertEquals(0, wrist.getLowerLimitInTicks(), 0.001);
        assertEquals(
                contract.getWristMaximumAngle()*wrist.getWristTicksPerDegree(), 
                wrist.getUpperLimitInTicks(), 
                0.001);
    }
    
    private void verifyWristPower(double expectedPower) {
        assertEquals(expectedPower, wrist.motor.getMotorOutputPercent(), 0.001);
    }
}
