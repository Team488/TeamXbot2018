package competition.subsystems.climberdeploy;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimberdeploySubsystem extends BaseSubsystem {
	
	DoubleProperty deploySpeed;
	
	@Inject
	public ClimberdeploySubsystem(XPropertyManager propMan) {
		propMan.createPersistentProperty("deploySpeed", 0.2);
	}
	
	/**
	 * extends the climber arm
	 */
	public void extendClimberArm() {
		double speed = deploySpeed.get();
	}
	
	/**
	 * detracts the climber arm
	 */
	public void detractClimberArm() {
		double speed = deploySpeed.get();
	}
	
	/**
	 * stops arm from moving or deploying
	 */
	public void stopClimberArm() {
		
	}
	
	/**
	 * speeds up the arm, regardless of what direction the arm is moving
	 */
	public void increaseSpeed() {
		
	}
	
	/**
	 * slows down the arm, regardless of what direction the arm is moving
	 */
	public void decreaseSpeed() {
	
	}
	
	/**
	 * sensor to determine when the climber arm has deployed to the correct height
	 */
	public boolean hitBarHeight() {
		return false;
	}
	
	/**
	 * sensor to determine when the climber arm is fully retracted 
	 */
	public boolean isRetracted() {
		return false;
	}
 }
