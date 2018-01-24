package competition.subsystems.gripperintake;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class GripperIntakeSubsystem extends BaseSubsystem {
	
	private static final int QuadEncoder = 0;
	CommonLibFactory clf;
	public XCANTalon rightMotor;
	public XCANTalon leftMotor;
	public XDigitalInput calibrationSensor;
	
	final DoubleProperty highPower;
	final DoubleProperty lowPower;
	
	@Inject
	public GripperIntakeSubsystem(CommonLibFactory clf, XPropertyManager propMan) {
		this.clf = clf;
		highPower = propMan.createPersistentProperty("Gripper Intake High Power", 1);
		lowPower = propMan.createPersistentProperty("Gripper Intake Low Power", 0.25);
	}
	
	public void temporaryHack() {
		rightMotor = clf.createCANTalon(40);
		leftMotor = clf.createCANTalon(40);
		rightMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		leftMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		calibrationSensor = clf.createDigitalInput(1);
	}
	
	/**
	 * Directly controls motor power
	 * @param power -1 intakes, +1 ejects
	 */
	
	public void maxOut() {
		rightMotor.simpleSet(highPower.get());
		leftMotor.simpleSet(highPower.get());
	}
	
	public void maxIn() {
		rightMotor.simpleSet(highPower.get()*-1);
		leftMotor.simpleSet(highPower.get()*-1);
	}
	
	public void halfRightIn() {
		rightMotor.simpleSet(lowPower.get());
		leftMotor.simpleSet(highPower.get());
	}
	
	public void halfLeftIn() {
		rightMotor.simpleSet(highPower.get());
		leftMotor.simpleSet(lowPower.get());
	}
	
	public void stop() {
		rightMotor.simpleSet(0);
		leftMotor.simpleSet(0);
	}
}
