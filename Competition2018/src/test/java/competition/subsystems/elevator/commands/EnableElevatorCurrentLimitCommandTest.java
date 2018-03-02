package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class EnableElevatorCurrentLimitCommandTest extends BaseCompetitionTest {
    ElevatorSubsystem elevator;
    EnableElevatorCurrentLimitCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        
        elevator = injector.getInstance(ElevatorSubsystem.class);
        command = injector.getInstance(EnableElevatorCurrentLimitCommand.class);
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
        
        assertTrue(elevator.getCurrentLimitState());
    }
    
}
