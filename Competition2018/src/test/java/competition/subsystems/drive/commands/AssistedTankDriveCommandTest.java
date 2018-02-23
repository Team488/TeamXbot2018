package competition.subsystems.drive.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class AssistedTankDriveCommandTest extends BaseCompetitionTest {

    AssistedTankDriveCommand command;
    DriveSubsystem drive;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(AssistedTankDriveCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void weakTest() {
        // the HeadingAssistModule is already well-tested - we just need to make sure
        // it's been invoked in some way.
        command.initialize();

        ((MockFTCGamepad) oi.driverGamepad).setLeftStick(new XYPair(0, 1));       
        ((MockFTCGamepad) oi.driverGamepad).setRightStick(new XYPair(0, 1));

        command.execute();

        assertEquals(1, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(1, drive.rightMaster.getMotorOutputPercent(), 0.001);

        timer.advanceTimeInSecondsBy(10);

        command.execute();
        command.execute();
        mockRobotIO.setGyroHeading(mockRobotIO.getGyroHeading() + 90);
        command.execute();

        assertEquals(1, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertTrue(drive.rightMaster.getMotorOutputPercent() < 0.5);
    }
}
