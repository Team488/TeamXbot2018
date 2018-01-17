package competition.subsystems.elevator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;


@Singleton
public class ElevatorSubsystem extends BaseSubsystem {
	
	double defaultElevatorPower;
	
	@Inject
	public ElevatorSubsystem() {
	
	}

	/**
	 * Raises the elevator. Power is controlled by a property.
	 */
	public void rise(){
		
	}
	

	/**
	 * Returns true if the robot is rising
	 * @return
	 */
	boolean isRising() {
		return false;
	}
	
	
	/**
	 * Lower the elevator. Power is controlled by a property.
	 */
	public void lower(){
	
	}
	
	/**
	 * Returns true if the robot is lowing
	 * @return
	 */
	boolean isLowing() {
		return false;
		
	}
	
	public void stop(){
		
	}
	

	/**
	 * Returns true if the robot is stopped
	 * @return
	 */
	boolean isStopping() {
		return false;
		
	}
	
	/**
	 * Returns true if the elevator is close to its maximum height.
	 */
	boolean isCloseToMaxmumHeight(){
		return false;
		}
	
	/**
	 * Returns true if the elevator is close to its minimum height.
	 */
	boolean isCloseToMinimumHeight(){
		return false;
	}
	

	public void moveToMaxHeight(){
		
	}
	
	public void moveTtoMinHeight(){
		
	}
	
}
	
