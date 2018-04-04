package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class CalibrateElevatorHereCommandTest extends BaseCompetitionTest {
    
    CalibrateElevatorHereCommand command;
    ElevatorSubsystem elevator;
    
    @Override
    public void setUp() {
        super.setUp();
        this.command = injector.getInstance(CalibrateElevatorHereCommand.class);
        this.elevator = injector.getInstance(ElevatorSubsystem.class);
    }
    
    @Test
    public void test() {
        ((MockCANTalon)elevator.master).setPosition(20);
        command.initialize();
        assertTrue(elevator.isCalibrated());
        assertEquals(((MockCANTalon)elevator.master).getPosition(), elevator.getLowerLimitInTicks(), 0.0001);
    }

}
