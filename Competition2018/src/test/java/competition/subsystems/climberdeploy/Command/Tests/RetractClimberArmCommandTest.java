package competition.subsystems.climberdeploy.Command.Tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.climberdeploy.commands.RetractClimberArmCommand;
import xbot.common.injection.BaseWPITest;

public class RetractClimberArmCommandTest extends BaseWPITest {

    ClimberDeploySubsystem deploy;
    RetractClimberArmCommand command;

    @Override
    public void setUp() {
        super.setUp();
        deploy = injector.getInstance(ClimberDeploySubsystem.class);
        command = injector.getInstance(RetractClimberArmCommand.class);
        deploy.temporaryHack();
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkRetractClimberArm() {
        assertEquals(0.0, deploy.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(-0.4, deploy.motor.getMotorOutputPercent(), 0.001);
    }
}
