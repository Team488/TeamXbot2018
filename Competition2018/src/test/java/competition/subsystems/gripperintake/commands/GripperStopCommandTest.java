package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperStopCommandTest extends BaseCompetitionTest {

    GripperStopCommand command;
    GripperIntakeSubsystem intake;
    
    @Override
    public void setUp() {
        super.setUp();
        
        command = injector.getInstance(GripperStopCommand.class);
        intake = injector.getInstance(GripperIntakeSubsystem.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void verifyPower() {
        intake.leftMotor.simpleSet(1);
        intake.rightMotor.simpleSet(1);
        
        command.initialize();
        command.execute();
        
        assertEquals(0, intake.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(0, intake.rightMotor.getMotorOutputPercent(), 0.001);
    }
    
}
