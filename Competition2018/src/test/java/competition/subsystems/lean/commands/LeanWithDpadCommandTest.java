package competition.subsystems.lean.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import competition.BaseCompetitionTest;
import competition.subsystems.lean.LeanSubsystem;

public class LeanWithDpadCommandTest extends BaseCompetitionTest {
    
    LeanWithDpadCommand command;
    LeanSubsystem lean;
    
    @Override
    public void setUp() {
        super.setUp();
        
        command = injector.getInstance(LeanWithDpadCommand.class);
        lean = injector.getInstance(LeanSubsystem.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void testDpad() {
        command.initialize();
        ((MockFTCGamepad) oi.driverGamepad).setPOV(90);
        command.execute();
        assertEquals(command.leanPowerProp.get(), lean.motor.getMotorOutputPercent(), 0.001);
        ((MockFTCGamepad) oi.driverGamepad).setPOV(270);
        command.execute();
        assertEquals(-command.leanPowerProp.get(), lean.motor.getMotorOutputPercent(), 0.001);
        
    }
}
