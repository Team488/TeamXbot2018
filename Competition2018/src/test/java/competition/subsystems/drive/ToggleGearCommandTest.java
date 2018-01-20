package competition.subsystems.drive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ToggleGearCommand;
import edu.wpi.first.wpilibj.MockSolenoid;

public class ToggleGearCommandTest extends BaseCompetitionTest {
	ToggleGearCommand command;
	ShiftSubsystem subsystem;
	MockSolenoid solenoid;

	@Override
	public void setUp() {
		super.setUp();

		command = injector.getInstance(ToggleGearCommand.class);
		subsystem = injector.getInstance(ShiftSubsystem.class);

		solenoid = ((MockSolenoid) subsystem.solenoid);
	}

	@Test
	public void testCommand() {
		// start with high gear
		subsystem.setGear(Gear.HIGH_GEAR);
		assertTrue(solenoid.get());
		// switch to low gear
		command.initialize();
		command.execute();
		// false if low gear
		assertFalse(solenoid.get());

		// switch to high gear
		command.initialize();
		command.execute();
		// true if high gear
		assertTrue(solenoid.get());
	}

}
