package competition.subsystems.elevator.commands;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class CalibrateElevatorTicksPerInchesCommandTest extends BaseCompetitionTest {

    CalibrateElevatorTicksPerInchCommand command;
    ElevatorSubsystem elevator;


    @Override
    public void setUp() {
        super.setUp();
        this.command = injector.getInstance(CalibrateElevatorTicksPerInchCommand.class);
        this.elevator = injector.getInstance(ElevatorSubsystem.class);
    }

    @Test
    public void maxAndMinTickTest() {

        command.initialize();
        ((MockCANTalon) elevator.master).setPosition(10);
        command.execute(); 
        ((MockCANTalon) elevator.master).setPosition(40);
        command.execute(); 
        ((MockCANTalon) elevator.master).setPosition(80);
        command.execute(); 
        ((MockCANTalon) elevator.master).setPosition(0);
        command.execute(); 
        assertEquals(80, command.maxTick, 1e-5);
        assertEquals(0, command.minTick, 1e-5);

    }

}
