package competition.commandgroups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.MoveElevatorToHeightAndStabilizeCommand;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.wrist.WristSubsystem;
import competition.subsystems.wrist.commands.WristUpCommand;
import edu.wpi.first.wpilibj.MockTimer;

public class AutoPutCubeOnScaleCommandGroupTest extends BaseCompetitionTest {

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
    }
    
    @Test
    public void unityTest() {
        command.initialize();
       
    }
    
}
