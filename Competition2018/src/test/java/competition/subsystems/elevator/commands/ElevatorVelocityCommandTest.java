package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;



import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;


public class ElevatorVelocityCommandTest extends BaseCompetitionTest{
    
    ElevatorVelocityCommand command;
    ElevatorSubsystem elevator;
    MockCANTalon talon;
    double oldThrottle;
    double delta1;
    double delta2;
    
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
        elevator.setTargetHeight(15);
        command.execute();
        System.out.println(command.throttle);
        System.out.println(elevator.getCurrentHeightInInches());
        
        oldThrottle = command.throttle;
        command.execute();
        System.out.println(command.throttle);
        System.out.println(elevator.getCurrentHeightInInches());
        assertTrue(oldThrottle - command.throttle < 0);
        assertTrue(-0.2 <= command.throttle && command.throttle <= 1);
        delta1 = command.throttle - oldThrottle;
        
        oldThrottle = command.throttle;
        command.execute();
        System.out.println(command.throttle);
        System.out.println(elevator.getCurrentHeightInInches());
        assertTrue(oldThrottle - command.throttle < 0);
        assertTrue(-0.2 <= command.throttle && command.throttle <= 1);
        delta2 = command.throttle - oldThrottle;
        //assertTrue(delta1 > delta2);
        
    }
    
}
