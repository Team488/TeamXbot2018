package competition.subsystems.gripperdeploy;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem.Side;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class GripperDeploySubsystem extends BaseSubsystem{
	final DoubleProperty gripperDeploySpeed;
	CommonLibFactory clf;
	
	public XCANTalon motor;
	
	@Inject GripperDeploySubsystem(CommonLibFactory clf, XPropertyManager propMan) {
		this.clf = clf;
		gripperDeploySpeed = propMan.createPersistentProperty("gripperDeploySpeed", .5);
	}
	
	public void temporaryHack() {
		motor = clf.createCANTalon(40);
	}
	
	/**
	 * angles the Gripper up
	 */
	public void deployUp() {
		motor.simpleSet(gripperDeploySpeed.get());
	}
	
	/**
	 * angles the Gripper down
	 */
	public void deployDown() {
		motor.simpleSet(-gripperDeploySpeed.get());
	}
	
	/**
	 * stops the Gripper
	 */
	public void stopGripper() {
		motor.simpleSet(0);
	}
	
	/**
	 * returns orientation of gripper
	 */
	public double getDeployOrientation() {
		return motor.getSelectedSensorPosition(0);
	}
}
