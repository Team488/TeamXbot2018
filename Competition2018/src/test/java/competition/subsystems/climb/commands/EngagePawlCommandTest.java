package competition.subsystems.climb.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climb.ClimbSubsystem;

public class EngagePawlCommandTest extends BaseCompetitionTest {

    ClimbSubsystem climb;
    EngagePawlCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        
        climb = injector.getInstance(ClimbSubsystem.class);
        command = injector.getInstance(EngagePawlCommand.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void verifyState() {
        command.initialize();
        command.execute();
        
        assertTrue(climb.solenoidA.getAdjusted());
        assertFalse(climb.solenoidB.getAdjusted());
    }
    
}
