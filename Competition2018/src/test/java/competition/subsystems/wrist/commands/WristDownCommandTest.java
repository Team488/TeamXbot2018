package competition.subsystems.wrist.commands;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.wrist.WristSubsystem;

public class WristDownCommandTest extends BaseCompetitionTest {

    WristSubsystem wrist;
    WristDownCommand command;

    @Override
    public void setUp() {
        super.setUp();

        wrist = injector.getInstance(WristSubsystem.class);
        command = injector.getInstance(WristDownCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkDeployDown() {
        wrist.setWristUp(true);
        
        command.initialize();
        command.execute();
        
        assertFalse(wrist.getUp());
    }
}
