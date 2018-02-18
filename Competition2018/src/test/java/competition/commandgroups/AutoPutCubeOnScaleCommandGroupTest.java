package competition.commandgroups;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import competition.subsystems.wrist.WristSubsystem;
import edu.wpi.first.wpilibj.MockTimer;
import xbot.common.command.XScheduler;

public class AutoPutCubeOnScaleCommandGroupTest extends BaseCompetitionTest {

    XScheduler XScheduler;
    AutoPutCubeOnScaleCommandGroup command;
    DriveSubsystem drive;
    WristSubsystem wrist;
    ElevatorSubsystem elevator;
    GripperIntakeSubsystem intake;
    MockTimer mockTimer;
    
    @Override
    public void setUp() {
        super.setUp();
        this.drive = injector.getInstance(DriveSubsystem.class);
        this.elevator = injector.getInstance(ElevatorSubsystem.class);
        this.intake = injector.getInstance(GripperIntakeSubsystem.class);
        this.mockTimer = injector.getInstance(MockTimer.class);
        this.wrist = injector.getInstance(WristSubsystem.class);
        this.command = injector.getInstance(AutoPutCubeOnScaleCommandGroup.class);
        this.XScheduler = injector.getInstance(XScheduler.class);
    }
    
    @Test
    public void unityTest() {
        command.start();
        XScheduler.run();
        assertEquals(1, drive.leftMaster.getMotorOutputPercent(), 0.001);
       
    }
    
}
