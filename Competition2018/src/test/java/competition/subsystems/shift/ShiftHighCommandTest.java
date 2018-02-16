package competition.subsystems.shift;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftHighCommand;
import edu.wpi.first.wpilibj.MockSolenoid;

public class ShiftHighCommandTest extends BaseCompetitionTest {
    ShiftHighCommand shiftHighCommand;
    ShiftSubsystem subsystem;
    MockSolenoid solenoid;

    @Override
    public void setUp() {
        super.setUp();
        shiftHighCommand = injector.getInstance(ShiftHighCommand.class);
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
}
