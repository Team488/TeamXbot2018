package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Inject;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;

public class GripperViaTriggersCommandTest extends BaseCompetitionTest {
    
    GripperIntakeSubsystem gripper;
    GripperViaTriggersCommand command;

    @Inject
    public void setUp() {
        super.setUp();
        gripper = injector.getInstance(GripperIntakeSubsystem.class);
        command = injector.getInstance(GripperViaTriggersCommand.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void beforePowerSpikeLeftTrigger() {
        command.initialize();
        for (double i = 0; i < 10; i++) {
            double power = (double) i / 100;
            ((MockFTCGamepad) oi.operatorGamepad).setRawAxis(2, power); //Left is 2, Right is 3
            
            command.execute();
            
            assertEquals(-power, gripper.leftMotor.getMotorOutputPercent(), 0.001);
            assertEquals(-power, gripper.rightMotor.getMotorOutputPercent(), 0.001);
        }
    }
    
    @Test
    public void beforePowerSpikeRightTrigger() {
        for (double i = 0; i > -10; i--) {
            double power = (double) i / 100;
            ((MockFTCGamepad) oi.operatorGamepad).setRawAxis(3, power);
            
            command.execute();
            
            assertEquals(power, gripper.leftMotor.getMotorOutputPercent(), 0.001);
            assertEquals(power, gripper.rightMotor.getMotorOutputPercent(), 0.001);
        }
    }
    
    @Test
    public void afterPowerSpikeLeftTrigger() {
        for (double i = -10; i >= -100; i--) {
            double axis = (double) i / 100;
            ((MockFTCGamepad) oi.operatorGamepad).setRawAxis(2, Math.abs(axis));
            double expected = -((0.5 / 0.9 * ((MockFTCGamepad)oi.operatorGamepad).getRawAxis(2)) + 0.4 / 0.9);
            
            command.execute();
            
            assertEquals(expected, gripper.leftMotor.getMotorOutputPercent(), 0.001);
            assertEquals(expected, gripper.rightMotor.getMotorOutputPercent(), 0.001);
        }
    }
    
    @Test
    public void afterPowerSpikeRightTrigger() {
        for (double i = 10; i <= 100; i++) {
            double axis = (double) i / 100;
            ((MockFTCGamepad) oi.operatorGamepad).setRawAxis(3, axis);
            double expected = ((0.5/0.9) * ((MockFTCGamepad) oi.operatorGamepad).getRawAxis(3)) + 0.4/0.9;
            
            command.execute();
            
            assertEquals(expected, gripper.leftMotor.getMotorOutputPercent(), 0.001);
            assertEquals(expected, gripper.rightMotor.getMotorOutputPercent(), 0.001);
        }
    }
}
