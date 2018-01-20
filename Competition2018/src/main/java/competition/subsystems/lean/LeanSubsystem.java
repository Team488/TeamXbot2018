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

	double currentLeanSpeed;
	DoubleProperty fastLeanSpeed;
	DoubleProperty slowLeanSpeed;
	CommonLibFactory clf;
	
	public XCANTalon motor;
	
	@Inject
	public LeanSubsystem(CommonLibFactory clf, XPropertyManager propMan) {
		this.clf = clf;
		currentLeanSpeed = 0.2;		
		slowLeanSpeed = propMan.createPersistentProperty("slowLeanSpeed", currentLeanSpeed/2);
		fastLeanSpeed = propMan.createPersistentProperty("fastLeanSpeed", currentLeanSpeed*2);
	}
	
	public void temporaryHack() {
		motor = clf.createCANTalon(40);
	}
	
	/**
	 * makes the climb arm lean left
	 */
	public void leanLeft() {
		motor.simpleSet(currentLeanSpeed);
	}
	
	/**
	 * makes the climb arm lean right
	 */
	public void leanRight() {
		motor.simpleSet(-currentLeanSpeed);
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
		motor.simpleSet(0);
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
		currentLeanSpeed = fastLeanSpeed.get();
	}
	
	/**
	 * decreases leaning arm speed
	 */
	public void decreaseSpeed() {
		currentLeanSpeed = slowLeanSpeed.get();
	}
}
