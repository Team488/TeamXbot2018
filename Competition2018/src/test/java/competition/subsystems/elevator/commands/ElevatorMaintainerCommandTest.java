package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.MockTimer;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class ElevatorMaintainerCommandTest extends BaseCompetitionTest {
    
    ElevatorSubsystem elevator;
    ElevatorMaintainerCommand command;
    MockTimer mockTimer;
    
    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(ElevatorMaintainerCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
        elevator.calibrateHere();
        mockTimer = injector.getInstance(MockTimer.class);
    }
    
    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void checkGoUp() {
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.isCalibrated();
        command.initialize();
        elevator.setTargetHeight(70);
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() > 0);
    }
   
    @Test
    public void checkGoDown() {
        ((MockCANTalon)elevator.motor).setPosition(3700);
        command.initialize();
        elevator.setTargetHeight(12);
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() < 0);
    }
    
    @Test
    public void stop() {
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(40);
        command.initialize();
        command.execute();
        assertEquals(0.0, elevator.motor.getMotorOutputPercent(), .01);
    }
    
    @Test
    public void checkCalibrateMode() {
        elevator.uncalibrate();
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(70);
        command.initialize();
        command.execute();
        assertEquals(-.2, elevator.motor.getMotorOutputPercent(), .01);
    }
    
    @Test
    public void checkMaintainerMode() {
        elevator.calibrateHere();
        ((MockCANTalon)elevator.motor).setPosition(3700);
        command.initialize();
        elevator.setTargetHeight(70);
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() > 0);
    }
    
    @Test
    public void checkManualMode() {
        elevator.uncalibrate();
        command.initialize();
        mockTimer.advanceTimeInSecondsBy(4000);
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(70);
        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, .15));
        command.execute();        
        assertEquals(.15, elevator.motor.getMotorOutputPercent(), .01);
    }
     
    @Test
    public void checkTryToCalUnderTime() {
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(70);
        elevator.uncalibrate();
        command.initialize();
        command.execute();
        assertEquals(-.2, elevator.motor.getMotorOutputPercent(), .01);
        elevator.calibrateHere();
        elevator.setTargetHeight(70);
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() > 0);
        elevator.uncalibrate();
        mockTimer.advanceTimeInSecondsBy(4000);
        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, .15));
        command.execute();
        assertEquals(0.15, elevator.motor.getMotorOutputPercent(), .01);
    }
    
    @Test
    public void checkTryToCalOverTime() {
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(70);
        elevator.uncalibrate();
        command.initialize();
        mockTimer.advanceTimeInSecondsBy(4000);
        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, .15));
        command.execute();
        assertEquals(.15, elevator.motor.getMotorOutputPercent(), .01);
        elevator.calibrateHere();
        elevator.setTargetHeight(70);
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() > 0);
        elevator.uncalibrate();
        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, .1));
        command.execute();
        assertEquals(.1, elevator.motor.getMotorOutputPercent(), .01);
    }
    
    @Test
    public void checkMotionMagicModeGoUp() {
        command.isMotionMagicMode(true);
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.isCalibrated();
        command.initialize();
        elevator.setTargetHeight(70);
        command.execute();
        assertEquals(6700, ((MockCANTalon)elevator.motor).getSetpoint(), 0.001);
    }
    
    @Test
    public void checkMotionMagicModeGoDown() {
        command.isMotionMagicMode(true);
        ((MockCANTalon)elevator.motor).setPosition(3700);
        command.initialize();
        elevator.setTargetHeight(12);
        command.execute();
        assertEquals(900, ((MockCANTalon)elevator.motor).getSetpoint(), 0.001);
    }
    
    @Test
    public void checkMotionMagicModestop() {
        command.isMotionMagicMode(true);
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(40);
        command.initialize();
        command.execute();
        assertEquals(3700, ((MockCANTalon)elevator.motor).getSetpoint(), 0.001);
    }
    
    @Test
    public void checkMotionMagicModeMaintainerMode() {
        command.isMotionMagicMode(true);
        elevator.calibrateHere();
        ((MockCANTalon)elevator.motor).setPosition(3700);
        command.initialize();
        elevator.setTargetHeight(70);
        command.execute();
        assertEquals(6700, ((MockCANTalon)elevator.motor).getSetpoint(), 0.001);
    }
    
    @Test
    public void checkMotionMagicModeTryToCalUnderTime() {
        command.isMotionMagicMode(true);
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(70);
        elevator.uncalibrate();
        command.initialize();
        command.execute();
        assertEquals(-.2, elevator.motor.getMotorOutputPercent(), .01);
        elevator.calibrateHere();
        elevator.setTargetHeight(70);
        command.execute();
        assertEquals(10400, ((MockCANTalon)elevator.motor).getSetpoint(), 0.001);
        elevator.uncalibrate();
        mockTimer.advanceTimeInSecondsBy(4000);
        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, .15));
        command.execute();
        assertEquals(0.15, elevator.motor.getMotorOutputPercent(), .01);
    }
    
    @Test
    public void checkMotionMagicModeTryToCalOverTime() {
        command.isMotionMagicMode(true);
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(70);
        elevator.uncalibrate();
        command.initialize();
        mockTimer.advanceTimeInSecondsBy(4000);
        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, .15));
        command.execute();
        assertEquals(.15, elevator.motor.getMotorOutputPercent(), .01);
        elevator.calibrateHere();
        elevator.setTargetHeight(70);
        command.execute();
        assertEquals(10400, ((MockCANTalon)elevator.motor).getSetpoint(), 0.001);
        elevator.uncalibrate();
        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, .1));
        command.execute();
        assertEquals(.1, elevator.motor.getMotorOutputPercent(), .01);
    }
}
