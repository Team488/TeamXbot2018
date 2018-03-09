package competition.subsystems.drive.command;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.VelocityArcadeDriveCommand;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class VelocityArcadeDriveCommandTest extends BaseCompetitionTest {

    VelocityArcadeDriveCommand command;
    DriveSubsystem drive;
    
    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(VelocityArcadeDriveCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
    }
    
    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void rotateRight() {
        ((MockFTCGamepad)oi.driverGamepad).setRightStick(new XYPair(1, 0));
        command.initialize();
        command.execute();
        
        assertTrue(drive.leftMaster.getMotorOutputPercent() > 0);
        assertTrue(drive.rightMaster.getMotorOutputPercent() < 0);
    }
    
    @Test
    public void goStraight() {
        ((MockFTCGamepad)oi.driverGamepad).setLeftStick(new XYPair(0, 1));
        command.initialize();
        command.execute();
        
        assertTrue(drive.leftMaster.getMotorOutputPercent() > 0);
        assertTrue(drive.rightMaster.getMotorOutputPercent() > 0);
    }
    
}
