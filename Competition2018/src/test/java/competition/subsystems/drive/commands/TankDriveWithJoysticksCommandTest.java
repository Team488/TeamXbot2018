package competition.subsystems.drive.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.subsystems.drive.DriveTestBase;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class TankDriveWithJoysticksCommandTest extends DriveTestBase {

    TankDriveWithJoysticksCommand command;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(TankDriveWithJoysticksCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void followsJoysticks() {
        for (int i = -100; i < 101; i++) {
            double power = (double) i / 100;
            command.initialize();
            ((MockFTCGamepad) oi.driverGamepad).setLeftStick(new XYPair(0, power));
            command.execute();
            assertEquals(power, drive.leftMaster.getMotorOutputPercent(), 0.001);
            ((MockFTCGamepad) oi.driverGamepad).setRightStick(new XYPair(0, power));
            command.execute();
            assertEquals(power, drive.rightMaster.getMotorOutputPercent(), 0.001);
        }
    }

}
