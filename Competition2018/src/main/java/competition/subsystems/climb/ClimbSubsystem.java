package competition.subsystems.climb;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimbSubsystem extends BaseSubsystem{

	final DoubleProperty ascendSpeed;
	final DoubleProperty decendSpeed;
	CommonLibFactory clf;
	public XCANTalon motor;
	
	@Inject
	public ClimbSubsystem(CommonLibFactory clf, XPropertyManager propMan) {
		this.clf = clf;
		ascendSpeed = propMan.createPersistentProperty("ascendSpeed", 1);
		decendSpeed = propMan.createPersistentProperty("decendSpeed", -.1);
	}
	
	public void temporaryHack() {
		motor = clf.createCANTalon(40);
	}
	
	/**
	 * moves the winch to pull the robot up
	 **/
	public void ascend(){
		motor.simpleSet(ascendSpeed.get());
	}

	/**
	 * moves the winch to let the robot down
	 */
	public void decend(){
		motor.simpleSet(decendSpeed.get());
	}
	
	/**
	 * stops the winch
	 */
	public void stop(){
		motor.simpleSet(0);
	}
}
