package competition.subsystems.shift;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftHighCommand;
import competition.subsystems.shift.commands.ShiftLowCommand;
import edu.wpi.first.wpilibj.MockSolenoid;
import xbot.common.injection.BaseWPITest;

public class ShiftHighAndLowCommandsTest extends BaseCompetitionTest {
	ShiftHighCommand shiftHighCommand;
	ShiftLowCommand shiftLowCommand;
	ShiftSubsystem subsystem;
	MockSolenoid solenoid;

	@Override
	public void setUp() {
		super.setUp();

		shiftHighCommand = injector.getInstance(ShiftHighCommand.class);
		shiftLowCommand = injector.getInstance(ShiftLowCommand.class);
		subsystem = injector.getInstance(ShiftSubsystem.class);

		solenoid = ((MockSolenoid) subsystem.solenoid);
	}

	@Test
	public void testShiftHighCommand() {
		// shift from low to high
		subsystem.setGear(Gear.LOW_GEAR);
		shiftHighCommand.initialize();
		assertTrue(solenoid.get());

		// shift from high to high
		shiftHighCommand.initialize();
		assertTrue(solenoid.get());
	}

	@Test
	public void testShiftLowCommand() {
		// shift from high to low
		subsystem.setGear(Gear.HIGH_GEAR);
		shiftLowCommand.initialize();
		assertFalse(solenoid.get());

		// shift from low to low
		shiftLowCommand.initialize();
		assertFalse(solenoid.get());
	}

}
