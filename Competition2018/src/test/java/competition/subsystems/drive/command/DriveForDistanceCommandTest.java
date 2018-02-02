package competition.subsystems.drive.command;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import competition.subsystems.drive.DriveTestBase;
import competition.subsystems.drive.DriveSubsystem.Side;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import xbot.common.controls.MockRobotIO;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class DriveForDistanceCommandTest extends DriveTestBase {
  
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void positiveDistanceTest() {
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);
        command.setDeltaDistance(5);

        command.initialize();
        command.execute();

        verifyDrivePositive();
    } 

    @Test
    public void negativeDistanceTest() {
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);
        command.setDeltaDistance(-5);

        command.initialize();
        command.execute();

        verifyDriveNegative();
    }

    @Test
    public void toleranceTest() {
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);

        command.initialize();
        command.setDeltaDistance(1.0);

        ((MockCANTalon)drive.rightMaster).setPosition((int)drive.getInchesToTicks(Side.Right,-1));

        command.execute();
        assertTrue(!command.isFinished());

        ((MockCANTalon)drive.rightMaster).setPosition((int)drive.getInchesToTicks(Side.Right,0.1));
        command.execute();
        mockTimer.advanceTimeInSecondsBy(0.6);
        command.execute();
        assertTrue(command.isFinished());
    }

    @Test
    public void driveStraightTest() {
        MockRobotIO mockIO = injector.getInstance(MockRobotIO.class);
        DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);

        mockIO.setGyroHeading(90);
        command.setDeltaDistance(10);

        command.initialize();
        command.execute();

        mockIO.setGyroHeading(80);
        command.execute();

        assertTrue(drive.leftMaster.getMotorOutputPercent() < ((MockCANTalon) drive.rightMaster).getMotorOutputPercent());

        mockIO.setGyroHeading(100);
        command.execute();

        assertTrue(((MockCANTalon) drive.rightMaster).getMotorOutputPercent() < ((MockCANTalon) drive.leftMaster).getMotorOutputPercent());
    }
}