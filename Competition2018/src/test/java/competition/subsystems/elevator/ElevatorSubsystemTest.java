package competition.subsystems.elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import edu.wpi.first.wpilibj.MockDigitalInput;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class ElevatorSubsystemTest extends BaseCompetitionTest {
	
	ElevatorSubsystem elevator;
	
	@Override
	public void setUp() {
		super.setUp();
		this.elevator=injector.getInstance(ElevatorSubsystem.class);
		this.elevator.temporaryHack();
	}

	@Test
	public void isStopped() {
		elevator.setPower(1);
		elevator.stop();
		checkElevatorPower(0);
	}
	
	@Test
	public void rise() {
		assertEquals(elevator.motor.getMotorOutputPercent(), 0, 0.001);
		elevator.rise();
		assertTrue(elevator.motor.getMotorOutputPercent() > 0);
	}
	
	@Test
	public void lower() {
		assertEquals(elevator.motor.getMotorOutputPercent(), 0, 0.001);
		elevator.lower();
		assertTrue(elevator.motor.getMotorOutputPercent() < 0);
	}
	
	@Test
	public void testCantLowerIfLimitSwitchPressed() {
	    ((MockDigitalInput)elevator.calibrationSensor).set_value(true);
	    elevator.setPower(-1);
	    
	    checkElevatorPower(0);
	}
	
	@Test
	public void testTypicalCalibration() {
	    // make sure we start uncalibrated at some random height
	    ((MockCANTalon)elevator.motor).setPosition(1000);
        assertTrue(elevator.currentHeight() > 5);
	    assertFalse(elevator.isCalibrated());
	    
	    elevator.setPower(-1);
	    // verify going down, but not at full power
	    assertTrue(elevator.motor.getMotorOutputPercent() > -0.8);
	    assertTrue(elevator.motor.getMotorOutputPercent() < -0.1);
	    
	    ((MockDigitalInput)elevator.calibrationSensor).set_value(true);
	    elevator.setPower(-1);
	    // verify that the system is now calibrated to the minimum height.
	    assertTrue(elevator.isCalibrated());
	    assertEquals(3, elevator.currentHeight(), 0.001);
	    
	    
	    // have the system raise but the switch still depress - this should not continuously calibrate.
	    // With the current hardcoded values, every 100 ticks is one inch of travel.
	    ((MockCANTalon)elevator.motor).setPosition(2000);
	    elevator.setPower(1);
	    assertTrue(elevator.isCalibrated());
        assertEquals(13, elevator.currentHeight(), 0.001);
	}
	
	private void checkElevatorPower(double power) {
		assertEquals(power, elevator.motor.getMotorOutputPercent(),  0.001);
	}
	
}
