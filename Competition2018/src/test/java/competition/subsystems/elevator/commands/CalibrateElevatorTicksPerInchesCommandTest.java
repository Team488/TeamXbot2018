package competition.subsystems.elevator.commands;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class CalibrateElevatorTicksPerInchesCommandTest extends BaseCompetitionTest {

    CalibrateElevatorTicksPerInchCommand command;

    @Override
    public void setUp() {
        super.setUp();
        this.command = injector.getInstance(CalibrateElevatorTicksPerInchCommand.class);
    }
    
    @Test
    public void maxTickTest() {
        
        command.initialize();
        
        
    }
    
}
