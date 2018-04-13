package competition.subsystems.drive.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class ArcadeDriveWithJoysticksCommandTest extends BaseCompetitionTest {

    ArcadeDriveWithJoysticksCommand command;
    DriveSubsystem drive;
    ElevatorSubsystem elevator;

    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(ArcadeDriveWithJoysticksCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
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
    
    @Test
    public void reduceAccelerationTest() {
        command.initialize();
        
        assertEquals(0, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0, drive.rightMaster.getMotorOutputPercent(), 0.001);
        
        command.execute();
        
        ((MockFTCGamepad) oi.driverGamepad).setLeftStick(new XYPair(1,1));
        ((MockCANTalon) elevator.master).setPosition((int) (elevator.getTargetScaleHighHeight() * 100) - 300);
        
        command.execute();
        
        assertEquals(0.2, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0.2, drive.rightMaster.getMotorOutputPercent(), 0.001);
        
        command.execute();
        
        assertEquals(0.4, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0.4, drive.rightMaster.getMotorOutputPercent(), 0.001);
        
        command.execute();
        
        assertEquals(0.6, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0.6, drive.rightMaster.getMotorOutputPercent(), 0.001);
        
        command.execute();
        
        assertEquals(0.8, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0.8, drive.rightMaster.getMotorOutputPercent(), 0.001);
        
        command.execute();
        
        assertEquals(1, drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(1, drive.rightMaster.getMotorOutputPercent(), 0.001);
    }
}
