package competition.subsystems.climb;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;

@Singleton
public class skeletonClimbSubsystem extends BaseSubsystem{
	
	 @Inject
	public skeletonClimbSubsystem() {
		// TODO Auto-generated constructor stub
	
		 }
	 
	 /**
	  * The arms will extend longer
	 **/
		  public void extend(){
	}
	
	/**
	 * The arms will withdraw 
	 */
		  public void withdraw(){
			  
	}
		  public void stop(){
			  
	}
	/**
	 * This boolean will determine if the arms extend close to the longest length
	 * @return
	 */
		  boolean isCloseLongest(){
			  return false;
			  
	}
	/**
	 * if the arms withdraw and it's close to the shortest length
	 * @return
	 */
		  boolean isCloseShortest(){
			  return false;
			  
	}
		  public void de
		  
}
