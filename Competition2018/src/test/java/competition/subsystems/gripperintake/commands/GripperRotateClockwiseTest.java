package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperRotateClockwiseTest extends BaseCompetitionTest{
    
    GripperRotateClockwiseCommand command;
    GripperIntakeSubsystem intake;
    
    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(GripperRotateClockwiseCommand.class);
        intake = injector.getInstance(GripperIntakeSubsystem.class);
    }
    
    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void verifyPower() {
        command.initialize();
        command.execute();
        
        assertEquals(-1.0, intake.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(1.0, intake.rightMotor.getMotorOutputPercent(), 0.001);
    }
}
