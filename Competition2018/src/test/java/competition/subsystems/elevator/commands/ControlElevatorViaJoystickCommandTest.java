package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class ControlElevatorViaJoystickCommandTest extends BaseElevatorTest {

    ControlElevatorViaJoystickCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        this.command = injector.getInstance(ControlElevatorViaJoystickCommand.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void followsJoystick() {
        assertEquals(0, elevator.motor.getMotorOutputPercent(), 0.001);
        
        ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, 1));
        command.initialize();
        command.execute();

        assertEquals(1, elevator.motor.getMotorOutputPercent(), 0.001);
        
        for (int i = -100; i < 101; i++) {
            double power = (double)i / 100;
            ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, power));
            command.execute();
            System.out.println(power);
            assertEquals(power, elevator.motor.getMotorOutputPercent(), 0.001);
        }
    }
}
