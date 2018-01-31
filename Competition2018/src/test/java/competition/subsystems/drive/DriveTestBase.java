package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;

import competition.BaseCompetitionTest;
import competition.subsystems.pose.PoseSubsystem;
import edu.wpi.first.wpilibj.MockTimer;

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
    
    public void initialize() {
    	
    }
}
