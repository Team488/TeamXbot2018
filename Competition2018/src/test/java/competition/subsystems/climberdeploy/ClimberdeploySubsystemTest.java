package competition.subsystems.climberdeploy;

import org.junit.Test;

import competition.BaseCompetitionTest;


public class ClimberdeploySubsystemTest extends BaseCompetitionTest {

    ClimberDeploySubsystem climberdeploy;
    
    @Override
    public void setUp() {
        super.setUp();
        this.climberdeploy = injector.getInstance(ClimberDeploySubsystem.class);
    }
}
