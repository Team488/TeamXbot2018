package competition.subsystems.climberdeploy;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.climberdeploy.commands.StopClimberArmCommand;

public class StopClimberArmCommandTest extends BaseCompetitionTest {

    ClimberDeploySubsystem deploy;
    StopClimberArmCommand command;

    @Override
    public void setUp() {
        super.setUp();

        deploy = injector.getInstance(ClimberDeploySubsystem.class);
        command = injector.getInstance(StopClimberArmCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkStopClimberArm() {
        deploy.extendClimberArm();
        assertEquals(0.4, deploy.motor.getMotorOutputPercent(), 0.001);
        command.initialize();
        command.execute();
        assertEquals(0.0, deploy.motor.getMotorOutputPercent(), 0.001);
    }
}