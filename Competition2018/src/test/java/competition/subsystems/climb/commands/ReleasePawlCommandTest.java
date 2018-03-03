package competition.subsystems.climb.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climb.ClimbSubsystem;

public class ReleasePawlCommandTest extends BaseCompetitionTest {

    ClimbSubsystem climb;
    ReleasePawlCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        
        climb = injector.getInstance(ClimbSubsystem.class);
        command = injector.getInstance(ReleasePawlCommand.class);
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
        
        assertFalse(climb.solenoidA.getAdjusted());
        assertTrue(climb.solenoidB.getAdjusted());
    }
    
}
