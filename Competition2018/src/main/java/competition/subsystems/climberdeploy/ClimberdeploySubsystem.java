package competition.subsystems.climberdeploy;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimberdeploySubsystem extends BaseSubsystem {
	
	DoubleProperty deploySpeed;
	DoubleProperty fastDeploySpeed;
	DoubleProperty slowDeploySpeed;
	CommonLibFactory clf;
	
	public XCANTalon motor;

	
	@Inject
	public ClimberdeploySubsystem(CommonLibFactory clf, XPropertyManager propMan) {
		this.clf = clf;
		deploySpeed = propMan.createPersistentProperty("deploySpeed", 0.2);
	}
	
	public void temporaryHack() {
		motor = clf.createCANTalon(40);
	}
	
	/**
	 * extends the climber arm
	 */
	public void extendClimberArm() {
		motor.simpleSet(deploySpeed.get());
	}
	
	/**
	 * detracts the climber arm
	 */
	public void retractClimberArm() {
		motor.simpleSet(-deploySpeed.get());
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
		motor.simpleSet(deploySpeed.get()*2);
	}
	
	/**
	 * slows down the arm, regardless of what direction the arm is moving
	 */
	public void decreaseSpeed() {
		motor.simpleSet(deploySpeed.get()/2);
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
