package competition.subsystems.climberdeploy.command.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.climberdeploy.commands.ExtendClimberArmCommand;
import xbot.common.injection.BaseWPITest;

public class ExtendClimberArmCommandTest extends BaseWPITest {

    ClimberDeploySubsystem deploy;
    ExtendClimberArmCommand command;

    @Override
    public void setUp() {
        super.setUp();
        deploy = injector.getInstance(ClimberDeploySubsystem.class);
        command = injector.getInstance(ExtendClimberArmCommand.class);
        deploy.temporaryHack();
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkExtendClimberArm() {
        assertEquals(0.0, deploy.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(0.4, deploy.motor.getMotorOutputPercent(), 0.001);
    }
}
