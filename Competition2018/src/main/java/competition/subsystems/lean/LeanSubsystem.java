package competition.subsystems.lean;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class LeanSubsystem extends BaseSubsystem {

	DoubleProperty leanSpeed;
	DoubleProperty fastLeanSpeed;
	DoubleProperty slowLeanSpeed;
	CommonLibFactory clf;
	
	public XCANTalon motor;
	
	@Inject
	public LeanSubsystem(CommonLibFactory clf, XPropertyManager propMan) {
		this.clf = clf;
		leanSpeed = propMan.createPersistentProperty("LeanSpeed", 0.2);
	}
	
	public void temporaryHack() {
		motor = clf.createCANTalon(40);
	}
	
	
	/**
	 * makes the climb arm lean left
	 */
	public void leanLeft() {
		motor.simpleSet(leanSpeed.get());
	}
	
	/**
	 * makes the climb arm lean right
	 */
	public void leanRight() {
		motor.simpleSet(-leanSpeed.get());

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
	public boolean hitBar() {
		return false;
	}
	
	/**
	 * increases leaning arm speed
	 */
	public void increaseSpeed() {
		motor.simpleSet(leanSpeed.get()*2);
	}
	
	/**
	 * decreases leaning arm speed
	 */
	public void decreaseSpeed() {
		motor.simpleSet(leanSpeed.get()/2);
	}
}
