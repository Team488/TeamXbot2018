package competition.subsystems.elevator.commands;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;

public class BaseElevatorCommandTest extends BaseCompetitionTest {

    protected ElevatorSubsystem elevator;
    
    @Override
    public void setUp() {
        super.setUp();
        
        this.elevator = injector.getInstance(ElevatorSubsystem.class);
        elevator.calibrateHere();
    }
}
