package competition.commandgroups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climb.ClimbSubsystem;
import xbot.common.command.XScheduler;

public class ClimbRetractCommandGroupTest extends BaseCompetitionTest{
    
    XScheduler xScheduler;
    EngageWinchAndLockPawlCommandGroup command;
    ClimbSubsystem climb;
    
    @Override
    public void setUp() {
        super.setUp();
        this.climb = injector.getInstance(ClimbSubsystem.class);
        this.command = injector.getInstance(EngageWinchAndLockPawlCommandGroup.class);
        this.xScheduler = injector.getInstance(XScheduler.class);
    }
    
    @Test
    public void test() {
        command.start();
        
        xScheduler.run();
        
        assertFalse(climb.solenoidA.getAdjusted());
        
        xScheduler.run();
        
        assertTrue(climb.solenoidB.getAdjusted());
        
        assertEquals(1.0, climb.motor.getMotorOutputPercent(), .001);
    }
    
}
