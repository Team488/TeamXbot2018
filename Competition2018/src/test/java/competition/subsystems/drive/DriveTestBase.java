package competition.subsystems.drive;

import static org.junit.Assert.assertTrue;

import competition.BaseCompetitionTest;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.MockTimer;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class DriveTestBase extends BaseCompetitionTest {
	
	protected DriveSubsystem drive;
	protected PoseSubsystem pose;
	protected MockTimer mockTimer;
	
    public void setUp() {        
        super.setUp();
        drive = injector.getInstance(DriveSubsystem.class);
        pose = injector.getInstance(PoseSubsystem.class);
        mockTimer = injector.getInstance(MockTimer.class);
        
        mockTimer.advanceTimeInSecondsBy(10);
        pose.updatePeriodicData();
    }
    
    public void setRobotHeading(double heading) {
    	pose.setCurrentHeading(heading);
    }
    
    public void verifyDrivePositive() {
        assertTrue(((MockCANTalon)drive.leftMaster).getMotorOutputPercent() > 0);
        assertTrue(((MockCANTalon)drive.rightMaster).getMotorOutputPercent() > 0);
    }
    
    public void verifyDriveNegative() {
        assertTrue(((MockCANTalon)drive.leftMaster).getMotorOutputPercent() < 0);
        assertTrue(((MockCANTalon)drive.rightMaster).getMotorOutputPercent() < 0);
    }
}
