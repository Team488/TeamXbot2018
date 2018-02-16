package competition.subsystems.drive.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import xbot.common.injection.BaseWPITest;
import xbot.common.subsystems.pose.BasePoseSubsystem;
import competition.BaseCompetitionTest;
import competition.subsystems.drive.commands.RotateToHeadingCommand;

public class RotateToHeadingCommandTest extends BaseCompetitionTest {
    
    RotateToHeadingCommand rotate;
    BasePoseSubsystem pose;

    @Override
    public void setUp() {
        super.setUp();
        rotate = injector.getInstance(RotateToHeadingCommand.class);
    }
    
    @Test
    public void testTurnLeft() {
        mockRobotIO.setGyroHeading(0);
        rotate.setGoalHeading(90);
        rotate.initialize();
        rotate.execute();
        assertEquals(.5, rotate.getRotatePower(), 0.001);
        
        mockRobotIO.setGyroHeading(179);
        rotate.setGoalHeading(-179);
        rotate.execute();
        assertEquals(.0111111, rotate.getRotatePower(), 0.001);
    }
    
    @Test
    public void testTurnRight() {
        mockRobotIO.setGyroHeading(0);
        rotate.setGoalHeading(-90);
        rotate.initialize();
        rotate.execute();
        assertEquals(-.5, rotate.getRotatePower(), 0.001);
        
        mockRobotIO.setGyroHeading(-179);
        rotate.setGoalHeading(179);
        rotate.execute();
        assertEquals(-.0111111, rotate.getRotatePower(), 0.001);
    }
}
