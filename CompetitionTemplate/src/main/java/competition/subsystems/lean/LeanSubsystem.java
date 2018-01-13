package competition.subsystems.lean;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class LeanSubsystem extends BaseSubsystem {

	DoubleProperty leanSpeed;
	
	@Inject
	public LeanSubsystem(XPropertyManager propMan) {
		propMan.createPersistentProperty("LeanSpeed", 0.2);
	}
	
	/**
	 * makes the climb arm lean left
	 */
	public void leanLeft() {
		double speed = leanSpeed.get();

	}
	
	/**
	 * makes the climb arm lean right
	 */
	public void leanRight() {
		double speed = leanSpeed.get();
		
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
	 * increases leaning arm speed
	 */
	public void increaseSpeed() {

	}
	
	/**
	 * decreases leaning arm speed
	 */
	public void decreaseSpeed() {
		
	}
}
