package competition.subsystems.pose;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class PoseSubsystemTest extends BaseCompetitionTest {
    
    PoseSubsystem pose;
    
    @Override
    public void setUp() {
        super.setUp();
        this.pose = injector.getInstance(PoseSubsystem.class);
    }
    
    @Test
    public void setPosition() {
        pose.setCurrentPosition(0, 0);
        assertEquals(0, pose.getFieldOrientedTotalDistanceTraveled().x, .01);
        assertEquals(0, pose.getFieldOrientedTotalDistanceTraveled().y, .01);
        
        pose.setCurrentPosition(20, 30);
        assertEquals(20, pose.getFieldOrientedTotalDistanceTraveled().x, .01);
        assertEquals(30, pose.getFieldOrientedTotalDistanceTraveled().y, .01);
    }

}
