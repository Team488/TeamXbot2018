package competition.commandgroups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.ElevatorMaintainerCommand;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import competition.subsystems.wrist.WristSubsystem;
import edu.wpi.first.wpilibj.MockTimer;
import xbot.common.command.XScheduler;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class AutoPutCubeOnSwitchCommandGroupTest extends BaseCompetitionTest {

    DriveSubsystem drive;
    AutoPutCubeOnSwitchCommandGroup command;
    ElevatorSubsystem elevator;
    ElevatorMaintainerCommand maintainer;
    GripperIntakeSubsystem intake;
    MockTimer mockTimer;
    WristSubsystem wrist;
    XScheduler xScheduler;
    
    @Override
    public void setUp() {
        super.setUp();
        this.drive = injector.getInstance(DriveSubsystem.class);
        this.command = injector.getInstance(AutoPutCubeOnSwitchCommandGroup.class);
        this.elevator = injector.getInstance(ElevatorSubsystem.class);
        this.intake = injector.getInstance(GripperIntakeSubsystem.class);
        this.maintainer = injector.getInstance(ElevatorMaintainerCommand.class);
        this.mockTimer = injector.getInstance(MockTimer.class);
        this.wrist = injector.getInstance(WristSubsystem.class);
        this.xScheduler = injector.getInstance(XScheduler.class);
    }
    
    @Test
    public void unityTest() {
        command.start();
        elevator.calibrateHere();
        maintainer.start();
        
        xScheduler.run();
        xScheduler.run();
        xScheduler.run();
        
        assertEquals(drive.getPositionalPid().getMaxOutput(), drive.rightMaster.getMotorOutputPercent(), 0.001);
        assertEquals(drive.getPositionalPid().getMaxOutput(), drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertEquals(1, elevator.motor.getMotorOutputPercent(), 0.001);
        
        ((MockCANTalon) drive.rightMaster).setPosition(243);
        ((MockCANTalon) drive.leftMaster).setPosition(243);
        /**
         * 100 is the conversion for ticks to inches, and 300 the ticks for 3 ft
         */
        ((MockCANTalon) elevator.motor).setPosition((int)(elevator.getTargetSwitchDropHeight() * 100) - 300);
        mockTimer.advanceTimeInSecondsBy(1000);
        
        xScheduler.run();
        xScheduler.run();
        xScheduler.run();
        
        mockTimer.advanceTimeInSecondsBy(1000);
        
        xScheduler.run();
        
        assertTrue(command.elevatorToSwitch.isFinished());
        assertTrue(command.driveToDistance.isFinished());
        
    }
}