package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class ExperimentalMotionMagicCommandTest extends BaseElevatorCommandTest {

    ExperimentMotionMagicCommand command;
    
    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(ExperimentMotionMagicCommand.class);
    }
    
    @Test
    public void testHeightCalculation() {
        elevator.motionMagicToHeight(13);
        
        assertEquals(1000, ((MockCANTalon)elevator.master).getSetpoint(), 0.001);
    }
}
