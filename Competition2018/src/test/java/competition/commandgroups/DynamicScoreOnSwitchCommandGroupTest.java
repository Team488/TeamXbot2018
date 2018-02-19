package competition.commandgroups;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.autonomous.GameDataSource;
import competition.subsystems.autonomous.MockGameDataAdapter;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.DriveTestBase;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;
import xbot.common.command.XScheduler;

public class DynamicScoreOnSwitchCommandGroupTest extends DriveTestBase {

    DynamicScoreOnSwitchCommandGroup commandgroup;
    XScheduler scheduler;
    DriveSubsystem drive;
    MockGameDataAdapter dataSource;
    
    @Override
    public void setUp() {
        super.setUp();
        
        this.commandgroup = injector.getInstance(DynamicScoreOnSwitchCommandGroup.class);
        this.scheduler = injector.getInstance(XScheduler.class);
        this.drive = injector.getInstance(DriveSubsystem.class);
        this.dataSource = (MockGameDataAdapter)injector.getInstance(GameDataSource.class);
    }
    
    @Test
    public void simpleTest() {
        commandgroup.start();
        scheduler.run();
        assertEquals("Crashes should be zero", 0, scheduler.getNumberOfCrashes(), 0.001);
        scheduler.run();
        assertEquals("Crashes should be zero", 0, scheduler.getNumberOfCrashes(), 0.001);
        scheduler.run();
        assertEquals("Crashes should be zero", 0, scheduler.getNumberOfCrashes(), 0.001);
        scheduler.run();
        assertEquals("Crashes should be zero", 0, scheduler.getNumberOfCrashes(), 0.001);
    }
    
    @Test
    public void rightSideTest() {
        dataSource.setOwnedSide(GameFeature.SWITCH_NEAR, OwnedSide.RIGHT);
        commandgroup.start();
        scheduler.run();
        scheduler.run();
        scheduler.run();
        
        assertEquals("Right side has 4 points", 4, commandgroup.pursuit.getPlannedPointsToVisit().size(), 0.001);
        
        verifyDrivePositive();
    }
    
    @Test
    public void leftSideTest() {
        dataSource.setOwnedSide(GameFeature.SWITCH_NEAR, OwnedSide.LEFT);
        commandgroup.start();
        scheduler.run();
        scheduler.run();
        scheduler.run();
        
        assertEquals("Left side has 8 points", 8, commandgroup.pursuit.getPlannedPointsToVisit().size(), 0.001);
        
        verifyDrivePositive();
    }
}
