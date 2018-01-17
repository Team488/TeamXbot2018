package competition.subsystems.elevator;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import xbot.common.injection.BaseWPITest;
import xbot.common.properties.BooleanProperty;

public class ElevatorSubsystemTest extends BaseWPITest{
	
	ElevatorSubsystem elevator;

	
	@Override
	public void setUp() {
		super.setUp();
		this.elevator=injector.getInstance(ElevatorSubsystem.class);
		
	}
	
	@Test
	public void ElevatorSubsystemTest() {
		// TODO Auto-generated constructor stub
	}
	


	@Test
	public void isStopped() {
		
		elevator.stop();
		assertTrue(elevator.isStopping());
	}
	
	@Test
	public void rise() {
		
		elevator.rise();
		assertTrue(elevator.isRising());
		assertFalse(elevator.isCloseToMaxmumHeight());
		assertFalse(elevator.isCloseToMinimumHeight());
	}
	
	@Test
	public void lower() {
		
		elevator.lower();
		assertTrue(elevator.isLowing());
		assertFalse(elevator.isCloseToMinimumHeight());
		assertFalse(elevator.isCloseToMaxmumHeight());
	}
	
	public void moveToMaxmumHeight() {
		
		elevator.moveToMaxHeight();
		assertFalse(elevator.isRising()||elevator.isCloseToMinimumHeight());
		
	}
	
	public void moveToMinimumHeight() {
		
		elevator.moveTtoMinHeight();
		assertFalse(elevator.isLowing()||elevator.isCloseToMaxmumHeight());
	}
	
}
