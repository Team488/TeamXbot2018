package competition.subsystems.wrist.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.wrist.WristSubsystem;
import competition.subsystems.wrist.commands.WristUpCommand;

public class WristUpCommandTest extends BaseCompetitionTest {

    WristSubsystem wrist;
    WristUpCommand command;

    @Override
    public void setUp() {
        super.setUp();

        wrist = injector.getInstance(WristSubsystem.class);
        command = injector.getInstance(WristUpCommand.class);
    }

    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }

    @Test
    public void checkDeployUp() {
        wrist.setWristUp(false);
        
        command.initialize();
        command.execute();
        
        assertTrue(wrist.getUp());
    }
}