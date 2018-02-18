package competition.subsystems.drive.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class ArcadeDriveWithJoysticksCommandTest extends BaseCompetitionTest {

    ArcadeDriveWithJoysticksCommand command;
    DriveSubsystem drive;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(ArcadeDriveWithJoysticksCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void test() {
        command.initialize();

        assertEquals(0, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0, drive.rightMaster.getMotorOutputPercent(), 0.001);


        ((MockFTCGamepad) oi.driverGamepad).setLeftStick(new XYPair(1, 1));   

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
