package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;

public class GripperViaTriggersCommandTest extends BaseCompetitionTest {
    GripperViaTriggersCommand command;
    GripperIntakeSubsystem intake;
    
    @Override
    public void setUp() {
        super.setUp();

        command = injector.getInstance(GripperViaTriggersCommand.class);
        intake = injector.getInstance(GripperIntakeSubsystem.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void verifyTriggers() {
        command.initialize();
        for(int i = 0; i < 101; i++) {
            double power = (double)i / 100;
            ((MockFTCGamepad)oi.operatorGamepad).setRawAxis(2, power);
            command.execute();
            assertEquals(-power*power, intake.rightMotor.getMotorOutputPercent(), 0.001);
            assertEquals(-power*power, intake.leftMotor.getMotorOutputPercent(), 0.001);
        }
        ((MockFTCGamepad)oi.operatorGamepad).setRawAxis(2, 0);
        for(int i = 0; i < 101; i++) {
            double power = (double)i / 100;
            ((MockFTCGamepad)oi.operatorGamepad).setRawAxis(3, power);
            command.execute();
            assertEquals(power*power, intake.rightMotor.getMotorOutputPercent(), 0.001);
            assertEquals(power*power, intake.leftMotor.getMotorOutputPercent(), 0.001);
        }
    }
}
