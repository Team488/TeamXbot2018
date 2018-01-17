package competition.subsystems.drive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftGearCommand;
import edu.wpi.first.wpilibj.MockSolenoid;
import xbot.common.injection.BaseWPITest;

public class ShiftGearCommandTest extends BaseWPITest {
	ShiftGearCommand command;
	ShiftSubsystem subsystem;
	MockSolenoid solenoid;

	@Override
	public void setUp() {
		super.setUp();

		command = injector.getInstance(ShiftGearCommand.class);
		subsystem = injector.getInstance(ShiftSubsystem.class);
		
		solenoid = ((MockSolenoid)subsystem.solenoid);
	}

	@Test
	public void testCommand() {
		// start with high gear
		subsystem.setGear(Gear.HIGH_GEAR);
		assertTrue(solenoid.isOn());
		// switch to low gear
		command.initialize();
		command.execute();
		// false if low gear
		assertFalse(solenoid.isOn());

		// switch to high gear
		command.initialize();
		command.execute();
		// true if high gear
		assertTrue(solenoid.isOn());
	}

}
