package competition.subsystems.drive;

import org.junit.Test;

import competition.BaseTest;

public class DriveSubsystemTest extends BaseTest {
	
	@Test
	public void testStartingDriveSubsystem() {
		injector.getInstance(DriveSubsystem.class);
	}
}
