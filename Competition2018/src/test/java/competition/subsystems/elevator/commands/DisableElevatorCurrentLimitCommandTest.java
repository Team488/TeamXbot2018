package competition.subsystems.elevator.commands;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class DisableElevatorCurrentLimitCommandTest extends BaseCompetitionTest {
    ElevatorSubsystem elevator;
    DisableElevatorCurrentLimitCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        elevator = injector.getInstance(ElevatorSubsystem.class);
        command = injector.getInstance(DisableElevatorCurrentLimitCommand.class);
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
        
    }

}
