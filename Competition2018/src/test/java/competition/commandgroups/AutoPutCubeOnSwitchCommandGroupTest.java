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
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class AutoPutCubeOnSwitchCommandGroupTest extends BaseCompetitionTest {

    DriveSubsystem drive;
    AutoPutCubeOnSwitchCommandGroup command;
    BasePoseSubsystem pose;
    ElevatorSubsystem elevator;
    ElevatorMaintainerCommand elevatorMaintainer;
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
        this.elevatorMaintainer = injector.getInstance(ElevatorMaintainerCommand.class);
        this.mockTimer = injector.getInstance(MockTimer.class);
        this.pose = injector.getInstance(BasePoseSubsystem.class);
        this.wrist = injector.getInstance(WristSubsystem.class);
        this.xScheduler = injector.getInstance(XScheduler.class);
    }

    @Test
    public void unityTest() {
        elevator.calibrateHere();
        elevatorMaintainer.start();
        command.start();

        xScheduler.run();
        xScheduler.run();
        xScheduler.run();

        assertEquals(drive.getPositionalPid().getMaxOutput(), drive.rightMaster.getMotorOutputPercent(), 0.001);
        assertEquals(drive.getPositionalPid().getMaxOutput(), drive.leftMaster.getMotorOutputPercent(), 0.001);
        assertTrue(elevator.motor.getMotorOutputPercent() > 0);

        ((MockCANTalon) drive.rightMaster).setPosition((int) (81.5 * (drive.getRightTicksPerFiveFt() / 60)));
        ((MockCANTalon) drive.leftMaster).setPosition((int) (81.5 * (drive.getLeftTicksPerFiveFt() / 60)));
        pose.updatePeriodicData();
        /**
         * 100 is the conversion for ticks to inches, and 300 the ticks for 3 ft
         */
        ((MockCANTalon) elevator.motor).setPosition((int) (elevator.getTargetSwitchDropHeight() * 100) - 300);
        mockTimer.advanceTimeInSecondsBy(1000);

        xScheduler.run();
        xScheduler.run();
        xScheduler.run();

        mockTimer.advanceTimeInSecondsBy(1000);

        xScheduler.run();
        xScheduler.run();
        xScheduler.run();

        mockTimer.advanceTimeInSecondsBy(2);
        xScheduler.run();
        mockTimer.advanceTimeInSecondsBy(2);
        xScheduler.run();
        mockTimer.advanceTimeInSecondsBy(2);
        xScheduler.run();

        assertEquals(0.25, intake.leftMotor.getMotorOutputPercent(), 0.001);
        assertEquals(0.25, intake.rightMotor.getMotorOutputPercent(), 0.001);
    }

}
