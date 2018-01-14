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
	 * Retracts the arms. Power is controlled by a property.
	 */
	public void retract(){

	}
	
	public void stop(){

	}
	
	/**
	 * Returns true if the arms extend close to the longest length
	 * @return
	 */
	boolean isCloseToLongest(){
		return false;

	}
	
	/**
	 * Returns true if the arms extend close to the shortest length
	 * @return
	 */
	boolean isCloseToShortest(){
		return false;

	}


}
