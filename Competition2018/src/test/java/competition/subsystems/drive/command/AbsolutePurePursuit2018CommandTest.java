package competition.subsystems.drive.command;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.autonomous.AutonomousPathSupplier;
import competition.subsystems.drive.commands.AbsolutePurePursuit2018Command;

public class AbsolutePurePursuit2018CommandTest extends BaseCompetitionTest {

    AbsolutePurePursuit2018Command command;
    
    @Override
    public void setUp() {
        super.setUp();
        command = injector.getInstance(AbsolutePurePursuit2018Command.class);
    }
    
    @Test
    public void simpleTest() {
        command.initialize();
        command.execute();
    }
    
    @Test
    public void advancedTest() {
        command.setAllPoints(injector.getInstance(AutonomousPathSupplier.class).createPathToLeftSwitchPlateNearestEdge());
        
        command.initialize();
        command.execute();
    }
}
