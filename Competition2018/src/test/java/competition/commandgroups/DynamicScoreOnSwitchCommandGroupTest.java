package competition.commandgroups;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.autonomous.AutonomousDecisionSystem;
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
    AutonomousDecisionSystem decider;
    
    @Override
    public void setUp() {
        super.setUp();
        
        this.commandgroup = injector.getInstance(DynamicScoreOnSwitchCommandGroup.class);
        this.scheduler = injector.getInstance(XScheduler.class);
        this.drive = injector.getInstance(DriveSubsystem.class);
        this.dataSource = (MockGameDataAdapter)injector.getInstance(GameDataSource.class);
        this.decider = injector.getInstance(AutonomousDecisionSystem.class); 
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
        
        assertEquals("Should be heading to right side", decider.createPathToRightSwitch().size(), commandgroup.pursuit.getPlannedPointsToVisit().size(), 0.001);
        
        verifyDrivePositive();
    }
    
    @Test
    public void leftSideTest() {
        dataSource.setOwnedSide(GameFeature.SWITCH_NEAR, OwnedSide.LEFT);
        commandgroup.start();
        scheduler.run();
        scheduler.run();
        scheduler.run();
        
        assertEquals("Should be heading to left side", decider.createPathToLeftSwitch().size(), commandgroup.pursuit.getPlannedPointsToVisit().size(), 0.001);
        
        verifyDrivePositive();
    }
}
