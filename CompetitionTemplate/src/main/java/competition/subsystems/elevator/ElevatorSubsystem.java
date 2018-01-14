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
	 * Lower the elevator. Power is controlled by a property.
	 */
	public void lower(){
	
	}
	
	public void stop(){
		
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
	
