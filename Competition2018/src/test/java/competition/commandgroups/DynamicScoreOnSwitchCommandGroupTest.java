package competition.commandgroups;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.subsystems.autonomous.AutonomousPathSupplier;
import competition.subsystems.autonomous.GameDataSource;
import competition.subsystems.autonomous.MockGameDataAdapter;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.DriveTestBase;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;
import xbot.common.command.XScheduler;

public class DynamicScoreOnSwitchCommandGroupTest extends DriveTestBase {

    ScoreOnSwitchCommandGroup commandgroup;
    XScheduler scheduler;
    DriveSubsystem drive;
    MockGameDataAdapter dataSource;
    AutonomousPathSupplier decider;
    
    @Override
    public void setUp() {
        super.setUp();
        
        this.commandgroup = injector.getInstance(ScoreOnSwitchCommandGroup.class);
        this.scheduler = injector.getInstance(XScheduler.class);
        this.drive = injector.getInstance(DriveSubsystem.class);
        this.dataSource = (MockGameDataAdapter)injector.getInstance(GameDataSource.class);
        this.decider = injector.getInstance(AutonomousPathSupplier.class); 
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
        
        assertEquals("Should be heading to right side", 
                decider.createPathToRightSwitchPlateNearestEdge().get(0).simplePoint.pose.getPoint().x, 
                commandgroup.pursuit.getPlannedPointsToVisit().get(0).pose.getPoint().x,
                0.001);
    }
    
    @Test
    public void leftSideTest() {
        dataSource.setOwnedSide(GameFeature.SWITCH_NEAR, OwnedSide.LEFT);
        commandgroup.start();
        scheduler.run();
        scheduler.run();
        scheduler.run();
        
        assertEquals("Should be heading to left side", 
                decider.createPathToLeftSwitchPlateNearestEdge().get(0).simplePoint.pose.getPoint().x, 
                commandgroup.pursuit.getPlannedPointsToVisit().get(0).pose.getPoint().x,
                0.001);
    }
}
