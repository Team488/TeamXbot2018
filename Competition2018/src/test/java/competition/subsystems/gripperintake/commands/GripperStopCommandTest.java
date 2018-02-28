package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperStopCommandTest extends BaseCompetitionTest {

    GripperStopCommand command;
    GripperIntakeSubsystem gripper;
    
    @Override
    public void setUp() {
        super.setUp();
        
        command = injector.getInstance(GripperStopCommand.class);
        gripper = injector.getInstance(GripperIntakeSubsystem.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void verifyPower() {
        command.initialize();
        command.execute();
        
        assertEquals(0, gripper.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(0, gripper.rightMotor.getMotorOutputPercent(), 0.001);
    }
    
}
