package competition.subsystems.climberdeploy.command.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.climberdeploy.commands.RetractClimberArmCommand;

public class RetractClimberArmCommandTest extends BaseCompetitionTest {

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
