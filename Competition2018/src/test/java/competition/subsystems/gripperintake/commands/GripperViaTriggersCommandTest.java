package competition.subsystems.gripperintake.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Inject;

import competition.BaseCompetitionTest;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;

public class GripperViaTriggersCommandTest extends BaseCompetitionTest {
    
    GripperIntakeSubsystem gripper;
    GripperViaTriggersCommand command;

    @Inject
    public GripperViaTriggersCommandTest() {
        super.setUp();
        gripper = injector.getInstance(GripperIntakeSubsystem.class);
        command = injector.getInstance(GripperViaTriggersCommand.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void beforePowerSpike() {
        command.initialize();
        for (double i = 0; i < 11; i++) {
            double power = (double) i / 100;
            ((MockFTCGamepad) oi.operatorGamepad).setRawAxis(2, power); //Left is 2, Right is 3
            System.out.println("Input is:  " + ((MockFTCGamepad)oi.operatorGamepad).getRawAxis(2));
            System.out.println("Output is: " + gripper.leftMotor.getMotorOutputPercent());
            command.execute();
            
            assertEquals(power, gripper.leftMotor.getMotorOutputPercent(), 0.001);
            assertEquals(power, gripper.rightMotor.getMotorOutputPercent(), 0.001);
        }
    }
}
