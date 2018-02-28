package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class ElevatorUncalibrateCommandTest extends BaseCompetitionTest {
    ElevatorSubsystem elevator;
    ElevatorUncalibrateCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        
        elevator = injector.getInstance(ElevatorSubsystem.class);
        command = injector.getInstance(ElevatorUncalibrateCommand.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void verifyCalibrate() {
        command.initialize();
        command.execute();
        
        assertFalse(elevator.isCalibrated());
    }

}
