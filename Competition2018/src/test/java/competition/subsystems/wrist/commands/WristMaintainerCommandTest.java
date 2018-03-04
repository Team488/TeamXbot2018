package competition.subsystems.wrist.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class WristMaintainerCommandTest extends BaseCompetitionTest {

    WristMaintainerCommand command;
    WristSubsystem wrist;
    
    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(WristMaintainerCommand.class);
        wrist = injector.getInstance(WristSubsystem.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void testMaintain() {
        wrist.calibrateHere();
        command.initialize();
        wrist.setTargetAngle(45);
        command.execute();
        
        assertEquals(-wrist.getMaximumAllowedPower(), wrist.motor.getMotorOutputPercent(), 0.001);
        
        ((MockCANTalon)wrist.motor).setPosition(-45);
        assertEquals(45, wrist.getWristAngle(), 0.001);
        
        command.execute();
        assertEquals(0, wrist.motor.getMotorOutputPercent(), 0.001);
    }
    
    @Test
    public void uncalibrated() {
        wrist.setTargetAngle(45);
        
        command.initialize();
        command.execute();
        // Should attempt to calibrate for a while.
        assertEquals(wrist.getUncalibratedPowerFactor(), wrist.motor.getMotorOutputPercent(), 0.001);
        
        // Then gives up, responds to joysticks.
        timer.advanceTimeInSecondsBy(5);
        command.execute();
        assertEquals(0, wrist.motor.getMotorOutputPercent(), 0.001);
        
        ((MockFTCGamepad)oi.operatorGamepad).setLeftStick(new XYPair(0, 1));
        command.execute();
        assertEquals(wrist.getUncalibratedPowerFactor(), wrist.motor.getMotorOutputPercent(), 0.001);
    }
}
