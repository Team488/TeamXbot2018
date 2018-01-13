package competition.subsystems.lean;

import com.google.inject.Inject;

import xbot.common.command.BaseSubsystem;
import xbot.common.properties.XPropertyManager;

public class LeanSubsystem extends BaseSubsystem {

	DoubleProperty leanSpeed;
	
	@Inject
	public LeanSubsystem(XPropertyManager propMan) {
		
	}
	
	/**
	 * makes the climb arm lean left
	 */
	public void leanLeft() {
		
	}
	
	/**
	 * makes the climb arm lean right
	 */
	public void leanRight() {
	
	}
	
	/**
	 * moves the arm to center position
	 */
	public void center() {
		
	}
	
	/**
	 * stops the climb arm from leaning left
	 */
	public void stopLean() {
		
	}
	/**
	 * sensor to determine if the arm has hit the bar
	 */
	public boolean HitBar() {
		return false;
	}
	
	/**
	 * slows down the speed of the leaning arm
	 */
	public void slowLean() {
		
	}
	
	/**
	 * speeds up the speed of the leaning arm
	 */
	public void fastLean() {
		
	}
}
