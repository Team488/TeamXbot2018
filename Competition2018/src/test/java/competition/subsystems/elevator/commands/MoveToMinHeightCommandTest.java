package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class MoveToMinHeightCommandTest extends BaseCompetitionTest {

    MoveToMinHeightCommand command;
    ElevatorSubsystem elevator;

    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(MoveToMinHeightCommand.class);
        elevator = injector.getInstance(ElevatorSubsystem.class);
        elevator.temporaryHack();
        elevator.setCalibrate(true);
    }
   
    @Test
    public void testSimple() {
        command.initialize();
        command.execute();
    }

    @Test
    public void verifyMovingDown() {
        command.initialize();
        command.execute();
        assertTrue(elevator.motor.getMotorOutputPercent() <= 0.1);
    }
    
    @Test
    public void stoppedAtTarget() {
        ((MockCANTalon)elevator.motor).setPosition(0);
        command.initialize();
        command.execute();
        assertEquals(0.0, elevator.motor.getMotorOutputPercent(), .01);
    }
    
    @Test
    public void uncalibratedBehavior() {
        elevator.setCalibrate(false);
        ((MockCANTalon)elevator.motor).setPosition(3700);
        elevator.setTargetHeight(12);
        command.initialize();
        command.execute();
        assertEquals(0.0, elevator.motor.getMotorOutputPercent(), .01);
    }
}
