package competition.subsystems.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xbot.common.injection.BaseWPITest;

public class DriveSubsystemTest extends BaseWPITest {
    @Test
    public void testTankDrive() {
        DriveSubsystem driveSubsystem = this.injector.getInstance(DriveSubsystem.class);
        driveSubsystem.tankDrive(1, 1);

        assertEquals(1, driveSubsystem.leftFrontDrive.get(), 0.001);
        assertEquals(1, driveSubsystem.leftRearDrive.get(), 0.001);
        assertEquals(1, driveSubsystem.rightFrontDrive.get(), 0.001);
        assertEquals(1, driveSubsystem.rightRearDrive.get(), 0.001);
    }
}
