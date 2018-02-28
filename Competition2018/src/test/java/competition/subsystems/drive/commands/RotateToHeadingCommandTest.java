package competition.subsystems.drive.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import xbot.common.subsystems.pose.BasePoseSubsystem;
import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.RotateToHeadingCommand;

public class RotateToHeadingCommandTest extends BaseCompetitionTest {
    
    RotateToHeadingCommand rotate;
    BasePoseSubsystem pose;
    DriveSubsystem drive;

    @Override
    public void setUp() {
        super.setUp();
        rotate = injector.getInstance(RotateToHeadingCommand.class);
        drive = injector.getInstance(DriveSubsystem.class);
    }
    
    @Test
    public void testTurnLeft() {
        mockRobotIO.setGyroHeading(0);
        rotate.setGoalHeading(90);
        rotate.initialize();
        rotate.execute();
        assertTrue((drive.leftMaster).getMotorOutputPercent() < (drive.rightMaster).getMotorOutputPercent());
        
        mockRobotIO.setGyroHeading(179);
        rotate.setGoalHeading(-179);
        rotate.execute();
        assertTrue((drive.leftMaster).getMotorOutputPercent() < (drive.rightMaster).getMotorOutputPercent());
    }
    
    @Test
    public void testTurnRight() {
        mockRobotIO.setGyroHeading(0);
        rotate.setGoalHeading(-90);
        rotate.initialize();
        rotate.execute();
        assertTrue((drive.leftMaster).getMotorOutputPercent() > (drive.rightMaster).getMotorOutputPercent());
        
        mockRobotIO.setGyroHeading(-179);
        rotate.setGoalHeading(179);
        rotate.execute();
        assertTrue((drive.leftMaster).getMotorOutputPercent() > (drive.rightMaster).getMotorOutputPercent());
    }
}
