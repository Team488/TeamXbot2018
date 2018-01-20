package competition.subsystems.drive;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import edu.wpi.first.wpilibj.MockSolenoid;

public class ShiftSubsystemTest extends BaseCompetitionTest {

	ShiftSubsystem subsystem;
	MockSolenoid solenoid;

	@Override
	public void setUp() {
		super.setUp();

		subsystem = injector.getInstance(ShiftSubsystem.class);
		solenoid = ((MockSolenoid) subsystem.solenoid);
	}

	@Test
	public void testSubsystem() {
		// shift high twice
		subsystem.setGear(Gear.HIGH_GEAR);
		subsystem.setGear(Gear.HIGH_GEAR);
		assertTrue(subsystem.gear == Gear.HIGH_GEAR);

		// shift low twice
		subsystem.setGear(Gear.LOW_GEAR);
		subsystem.setGear(Gear.LOW_GEAR);
		assertTrue(subsystem.gear == Gear.LOW_GEAR);
	}

}
