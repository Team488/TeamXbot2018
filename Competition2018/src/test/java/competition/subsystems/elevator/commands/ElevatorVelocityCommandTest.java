package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem.Side;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class ElevatorVelocityCommandTest extends BaseCompetitionTest{
    
    ElevatorVelocityCommand command;
    ElevatorSubsystem elevator;
    MockCANTalon talon;
    
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
    
    @Test
    public void testVelocity() {
        command.initialize();
        ((MockCANTalon) elevator.motor).setPosition(0);
        command.execute();
        assertTrue(-0.2 <= command.throttle && command.throttle <= 1);
        //assertEquals(
    }
    
}
