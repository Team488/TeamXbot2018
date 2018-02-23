package competition.subsystems.wrist.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.wrist.WristSubsystem;

public class SetWristAngleCommandTest extends BaseCompetitionTest {

    SetWristAngleCommand command;
    WristSubsystem wrist;
    
    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(SetWristAngleCommand.class);
        wrist = injector.getInstance(WristSubsystem.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void canSet() {
        assertEquals(90, wrist.getTargetAngle(), 0.001);
        
        command.setGoalAngle(33);
        command.initialize();
        
        assertEquals(33, wrist.getTargetAngle(), 0.001);
    }
}
