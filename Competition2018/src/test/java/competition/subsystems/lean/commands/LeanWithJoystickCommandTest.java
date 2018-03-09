package competition.subsystems.lean.commands;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.LeanWithDpadCommand;

public class LeanWithJoystickCommandTest extends BaseCompetitionTest {

    LeanWithDpadCommand command;
    LeanSubsystem lean;

    @Override
    public void setUp() {
        super.setUp();

        lean = injector.getInstance(LeanSubsystem.class);
        command = injector.getInstance(LeanWithDpadCommand.class);
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