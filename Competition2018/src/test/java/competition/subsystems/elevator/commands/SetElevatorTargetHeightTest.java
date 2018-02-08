package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class SetElevatorTargetHeightTest extends BaseCompetitionTest {

    ElevatorSubsystem elevator;
    SetElevatorTargetHeightCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        
        command = injector.getInstance(SetElevatorTargetHeightCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
        //elevator.temporaryHack();
    }
    
    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void checkTransaction() {
        elevator.setTargetHeight(79);
        assertEquals(79, elevator.getTargetHeight(), .01);
        command.setGoalHeight(10);
        command.initialize();
        command.execute();
        assertEquals(10, elevator.getTargetHeight(), .01);
        assertTrue(command.isFinished());
    }
}
