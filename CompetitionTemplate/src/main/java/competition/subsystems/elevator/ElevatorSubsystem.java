package competition.subsystems.elevator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;


@Singleton
public class ElevatorSubsystem extends BaseSubsystem {
	
	@Inject
	public ElevatorSubsystem() {
		// TODO Auto-generated constructor stub
	
	}

	/**
	 * the elevator will rise by certain speed, the speed may change
	 */
	public void rise(){
		
	}
	
	
	/**
	 * the elevator will fail by certain speed, the speed may change
	 */
	public void lower(){
	//move when hold the button??
	}
	
	public void stop(){
		
	}
	
	/**
	 * if it's close to the max height
	 */
	boolean isCloseHighest(){
		return false;
		}
	
	/**
	 * if it's close to the min height
	 */
	boolean isCloseLowest(){
		return false;
	}
	

	public void toMaxHeight(){
		
	}
	public void toMinHeight(){
		
	}
	
}
	
