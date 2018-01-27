package competition.subsystems.climberdeploy.command.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.climberdeploy.commands.DecreaseSpeedCommand;
import xbot.common.injection.BaseWPITest;

public class DecreaseSpeedCommandTest extends BaseWPITest {

    ClimberDeploySubsystem deploy;
    DecreaseSpeedCommand command;

    @Override
    public void setUp() {
        super.setUp();

        deploy = injector.getInstance(ClimberDeploySubsystem.class);
        command = injector.getInstance(DecreaseSpeedCommand.class);
        deploy.temporaryHack();
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkDecreaseSpeed() {
        deploy.extendClimberArm();
        assertEquals(0.4, deploy.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        deploy.extendClimberArm();
        assertEquals(0.1, deploy.motor.getMotorOutputPercent(), 0.001);
    }
}