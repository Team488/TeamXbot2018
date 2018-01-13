package competition.subsystems.climb;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;

@Singleton
public class skeletonClimbSubsystem extends BaseSubsystem{

	double defaultClimbPower;

	@Inject
	public skeletonClimbSubsystem() {


	}

	/**
	 * Extends the arms. Power is controlled by a property.
	 **/
	public void extend(){
	}

	/**
	 * Withdraws the arms. Power is controlled by a property.
	 */
	public void withdraw(){

	}
	
	public void stop(){

	}
	
	/**
	 * Returns true if the arms extend close to the longest length
	 * @return
	 */
	boolean isCloseToMaxmumHeight(){
		return false;

	}
	
	/**
	 * Returns true if the arms extend close to the shortest length
	 * @return
	 */
	boolean isCloseToMinimumHeight(){
		return false;

	}


}
