package competition.subsystems.elevator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.sensors.XDigitalInput;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;


@Singleton
public class ElevatorSubsystem extends BaseSubsystem {
	
	double defaultElevatorPower;
	CommonLibFactory clf;
	final DoubleProperty elevatorPower;
	final DoubleProperty elevatorTicksPerInch;
	
	/**
	 * If our elevator is uncalibrated, we don't allow large power inputs.
	 */
	final DoubleProperty calibrationPower;
	
	private boolean _isCalibrated;
	private double calibrationOffset;
	private Latch calibrationLatch;
	
	double height;
	double maxHeight;
	double minHeight;
	
	public XCANTalon motor;
	public XDigitalInput calibrationSensor;
	
	@Inject
	public ElevatorSubsystem(CommonLibFactory clf, XPropertyManager propMan) {
		this.clf = clf;
		elevatorPower = propMan.createPersistentProperty("ElevatorPower", 0.4);
		elevatorTicksPerInch = propMan.createPersistentProperty("ElevatorTicksPerInch", 100);
		calibrationPower = propMan.createPersistentProperty("ElevatorCalibrationPower", 0.2);
		calibrationOffset = 0;
	}
	
	public void temporaryHack() {
		motor = clf.createCANTalon(40);
		calibrationSensor = clf.createDigitalInput(1);
		
		calibrationLatch = new Latch(false, EdgeType.RisingEdge);
		//calibrationLatch.addEdgeCallback(callback);
	}
	
	public void setPower(double power) {
	    
	    
	    
	    if (!_isCalibrated) {
	        MathUtils.constrainDouble(power, -calibrationPower.get(), calibrationPower.get());
	        
	        _isCalibrated = calibrationSensor.get();
	    }
	    
	    motor.simpleSet(power);
	}

	/**
	 * Raises the elevator. Power is controlled by a property.
	 */
	public void rise(){
	    setPower(elevatorPower.get());
	}
	
	
	/**
	 * Lower the elevator. Power is controlled by a property.
	 */
	public void lower(){
	    setPower(-elevatorPower.get());
	}
	
	public void stop(){
	    setPower(0);
	}
	
	public double currentHeight() {
		return ticksToInches(motor.getSelectedSensorPosition(0));
	}
	
	private double ticksToInches(double ticks) { 
	    double tpi = elevatorTicksPerInch.get();
	    if (tpi == 0) {
	        return 0;
	    }
	    
	    return (ticks - calibrationOffset) / tpi;
	}
	
	public boolean isCalibrated() {
	    return _isCalibrated;
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
}
	
