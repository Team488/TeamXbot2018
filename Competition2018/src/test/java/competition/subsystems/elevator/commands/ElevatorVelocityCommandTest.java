package competition.subsystems.elevator.commands;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class ElevatorVelocityCommandTest extends BaseCompetitionTest{
    
    ElevatorVelocityCommand command;
    ElevatorSubsystem elevator;
    
    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(ElevatorVelocityCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
    }


    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }
    
}
