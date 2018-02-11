package competition.subsystems.wrist.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.wrist.WristSubsystem;
import competition.subsystems.wrist.commands.WristDownCommand;

public class WristDownCommandTest extends BaseCompetitionTest {

    WristSubsystem gripperDeploy;
    WristDownCommand command;

    @Override
    public void setUp() {
        super.setUp();

        gripperDeploy = injector.getInstance(WristSubsystem.class);
        command = injector.getInstance(WristDownCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkDeployDown() {
        assertEquals(0.0, gripperDeploy.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(-1, gripperDeploy.motor.getMotorOutputPercent(), 0.001);
    }
}
