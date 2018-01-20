package competition.subsystems.elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import xbot.common.injection.BaseWPITest;

public class ElevatorSubsystemTest extends BaseWPITest{
	
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
	
	private void checkElevatorPower(double power) {
		assertEquals(elevator.motor.getMotorOutputPercent(), power, 0.001);
	}
}
