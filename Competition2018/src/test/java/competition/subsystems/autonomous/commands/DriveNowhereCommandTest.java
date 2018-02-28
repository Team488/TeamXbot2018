package competition.subsystems.autonomous.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;

public class DriveNowhereCommandTest extends BaseCompetitionTest {

    DriveSubsystem drive;
    DriveNowhereCommand command;
    
    public void setUp() {
        super.setUp();
        
        drive = injector.getInstance(DriveSubsystem.class);
        command = injector.getInstance(DriveNowhereCommand.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void verifyPower() {
        command.initialize();
        command.execute();
        
        assertEquals(0, drive.rightMaster.getMotorOutputPercent(), 0.001);
        assertEquals(0, drive.leftMaster.getMotorOutputPercent(), 0.001);
    }
    
}
