package competition.subsystems.drive;

import org.junit.Test;

import competition.BaseCompetitionTest;

public class DriveSubsystemTest extends BaseCompetitionTest {

    @Test
    public void testStartingDriveSubsystem() {
        injector.getInstance(DriveSubsystem.class);
    }
}
