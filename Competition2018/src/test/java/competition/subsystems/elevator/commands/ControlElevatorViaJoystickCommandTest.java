package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;
//import competition.subsystems.elevator.ElevatorSubsystem;

public class ControlElevatorViaJoystickCommandTest extends BaseElevatorCommandTest {

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
            //sets height to 43 inches and tests all powers
            elevator.calibrateAt(-4000);
            ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, power));
            command.execute();
            System.out.println(power);
            System.out.println(elevator.getCurrentHeightInInches());

            assertEquals(power, elevator.motor.getMotorOutputPercent(), 0.001);
        }

    }

    @Test
    public void slowsDownNearTop() {
        for (int i = 30; i < 101; i++) {
            double power = (double)i / 100;
            //sets height to 73 inches and tests powers above .3
            elevator.calibrateAt(-7000);
            ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, power));
            command.execute();
            System.out.println(power);
            System.out.println(elevator.getCurrentHeightInInches());

            assertEquals(elevator.getPowerNearHighLimit(), elevator.motor.getMotorOutputPercent(), 0.001);
        }
    }

    @Test
    public void slowsDownNearBottom() {
        //height is 3 inches, tests for powers below -.3
        for (int i = -100; i < -30; i++) {
            double power = (double)i / 100;
            ((MockFTCGamepad)oi.operatorGamepad).setRightStick(new XYPair(0, power));
            command.execute();
            System.out.println(power);
            System.out.println(elevator.getCurrentHeightInInches());

            assertEquals(elevator.getPowerNearLowLimit(), elevator.motor.getMotorOutputPercent(), 0.001);
        }
    }
}
