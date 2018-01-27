package competition.subsystems.drive.command;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.drive.DriveTestBase;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import edu.wpi.first.wpilibj.MockTimer;

public class DriveForDistanceCommandTest extends BaseCompetitionTest {
	MockTimer mockTimer;
	
	@Before
	public void setup() {
		super.setUp();
		mockTimer = injector.getInstance(MockTimer.class);
	}
	
	@Test
	public void positiveDistanceTest() {
		DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);
		command.setDeltaDistance(5);
		
		command.initialize();
		command.execute();
		
		assertTrue(Math.abs(command.leftPower) > 0);
		assertTrue(Math.abs(command.rightPower) > 0);
	}
	
	@Test
	public void negativeDistanceTest() {
		DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);
		command.setDeltaDistance(-5);
		
		command.initialize();
		command.execute();
		
		assertTrue(command.leftPower < 0);
		assertTrue(command.rightPower < 0);
	}
	
	@Test
	public void toleranceTest() {
		DriveForDistanceCommand command = injector.getInstance(DriveForDistanceCommand.class);
		
		command.initialize();
		command.setDeltaDistance(1.0);
		
		drive.rightDrive.setPosition(drive.convertInchesToTicks(-1));
	}
	
}
