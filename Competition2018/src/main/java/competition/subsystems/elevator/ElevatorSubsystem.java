package competition.subsystems.elevator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;


@Singleton
public class ElevatorSubsystem extends BaseSubsystem {
	
	double defaultElevatorPower;
	CommonLibFactory clf;
	
	double height;
	double maxHeight;
	double minHeight;
	
	public XCANTalon motor;
	
	@Inject
	public ElevatorSubsystem(CommonLibFactory clf) {
		this.clf = clf;
	}
	
	public void temporaryHack() {
		motor = clf.createCANTalon(40);
	}

	/**
	 * Raises the elevator. Power is controlled by a property.
	 */
	public void rise(){
		motor.simpleSet(0.4);
	}
	
	
	/**
	 * Lower the elevator. Power is controlled by a property.
	 */
	public void lower(){
		motor.simpleSet(-0.4);
	}
	
	public void stop(){
		motor.simpleSet(0);
	}
	
	public double currentHeight() {
		return height;
	}
	
	/**
	 * Returns true if the elevator is close to its maximum height.
	 */
	
	boolean isCloseToMaxmumHeight() {
		
		if (height >= maxHeight * 0.9) {
			return true;
		}
	
		return false;
		
	}
	
	/**
	 * Returns true if the elevator is close to its minimum height.
	 */
	
	boolean isCloseToMinimumHeight() {
		
		if (height < maxHeight * 0.15) {
			return true;
		}
		
		return false;
	}

	public void moveToMaxHeight() {
		motor.simpleSet(0.4);
	}
	
	public void moveToMinHeight() {
		motor.simpleSet(-0.4);
	}
	
	public void setPower(double power) {
		motor.simpleSet(power);
	}
	
}
	
