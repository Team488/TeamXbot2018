package competition.subsystems.wrist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj.MockDigitalInput;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class WristSubsystemTest extends BaseCompetitionTest {

    WristSubsystem wrist;

    @Override
    public void setUp() {
        super.setUp();

        wrist = injector.getInstance(WristSubsystem.class);
    }
}
