package competition.subsystems.shift;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftLowCommand;
import edu.wpi.first.wpilibj.MockSolenoid;

public class ShiftLowCommandTest extends BaseCompetitionTest {
    ShiftLowCommand shiftLowCommand;
    ShiftSubsystem subsystem;
    MockSolenoid solenoid;

    @Override
    public void setUp() {
        super.setUp();
        shiftLowCommand = injector.getInstance(ShiftLowCommand.class);
        subsystem = injector.getInstance(ShiftSubsystem.class);
        solenoid = ((MockSolenoid) subsystem.solenoid);
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
