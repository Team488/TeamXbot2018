package competition.subsystems.lean.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.LeanWithJoystickCommand;
import xbot.common.controls.sensors.mock_adapters.MockFTCGamepad;
import xbot.common.math.XYPair;

public class LeanWithJoystickCommandTest extends BaseCompetitionTest {

    LeanWithJoystickCommand command;
    LeanSubsystem lean;

    @Override
    public void setUp() {
        super.setUp();

        lean = injector.getInstance(LeanSubsystem.class);
        command = injector.getInstance(LeanWithJoystickCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
/*
    @Test
    public void followJoysticks() {
        command.initialize();

        for (int i = -100; i < 100; i++) {
            double power = (double) i / 100;
            ((MockFTCGamepad) oi.operatorGamepad).setLeftStick(new XYPair(power, 0));
            command.execute();
            assertEquals(power, lean.motor.getMotorOutputPercent(), 0.001);
        }
    }*/
}