package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class ControlElevatorViaJoystickCommandTest extends BaseElevatorCommandTest {

    ControlElevatorViaJoystickCommand command;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(ControlElevatorViaJoystickCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void followsJoystick() {
        command.initialize();
        for (int i = -100; i < 101; i++) {
            double power = (double) i / 100;
            elevator.calibrateAt(-5000);
            ((MockFTCGamepad) oi.operatorGamepad).setRightStick(new XYPair(0, power));
            command.execute();
            assertEquals(power, elevator.motor.getMotorOutputPercent(), 0.001);
        }
    }

    @Test
    public void limitPowerNearTop() {
        command.initialize();
        for (int i = 30; i < 101; i++) {
            double power = (double) i / 100;
            elevator.calibrateAt(-7000);
            ((MockFTCGamepad) oi.operatorGamepad).setRightStick(new XYPair(0, power));
            command.execute();
            assertEquals(elevator.getPowerNearHighLimit(), elevator.motor.getMotorOutputPercent(), 0.001);
        }
    }

    @Test
    public void limitPowerNearBottom() {
        command.initialize();
        for (int i = -100; i < -30; i++) {
            double power = (double) i / 100;
            elevator.calibrateAt(-100);
            ((MockFTCGamepad) oi.operatorGamepad).setRightStick(new XYPair(0, power));
            command.execute();
            assertEquals(elevator.getPowerNearLowLimit(), elevator.motor.getMotorOutputPercent(), 0.001);
        }
    }

}
