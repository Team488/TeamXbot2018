package competition.subsystems.shift;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.subsystems.shift.commands.ShiftHighCommand;
import competition.subsystems.shift.commands.ShiftLowCommand;
import edu.wpi.first.wpilibj.MockSolenoid;
import xbot.common.injection.BaseWPITest;

public class ShiftHighAndLowCommandsTest extends BaseWPITest {
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
		shiftHighCommand.initialize();
		assertTrue(solenoid.get());
	}

	@Test
	public void testShiftLowCommand() {
		shiftLowCommand.initialize();
		assertFalse(solenoid.get());
	}

}
