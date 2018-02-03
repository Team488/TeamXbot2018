package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class MaintainerCommandTest extends BaseCompetitionTest {
    
    ElevatorSubsystem elevator;
    ElevatorMaintainerCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        
        command = injector.getInstance(ElevatorMaintainerCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
        elevator.temporaryHack();
        
    }
    
    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void checkGoUp() {
        
        ((MockCANTalon)elevator.motor).setPosition(1200);
        elevator.setTargetHeight(70);
        command.initialize();
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() > 0);
    }
    
    @Test
    public void checkGoDown() {
        ((MockCANTalon)elevator.motor).setPosition(7000);
        elevator.setTargetHeight(12);
        command.initialize();
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() < 0);
    }
    
    @Test
    public void stop() {
        ((MockCANTalon)elevator.motor).setPosition(7000);
        elevator.setTargetHeight(70);
        command.initialize();
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() <= .01);
    }
}
