package competition.subsystems.elevator;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;


@Singleton
public class ElevatorSubsystem extends BaseSubsystem {
    
    double defaultElevatorPower;
    public XCANTalon motor;
    CommonLibFactory clf;
    final DoubleProperty elevatorPower;
    
    @Inject
    public ElevatorSubsystem(CommonLibFactory clf, XPropertyManager propMan) {
        this.clf = clf;
        elevatorPower = propMan.createPersistentProperty("ElevatorPower", 0.4);
    }
    
    public void temporaryHack() {
        motor = clf.createCANTalon(41);
    }

    /**
     * Raises the elevator. Power is controlled by a property.
     */
    public void rise(){
        motor.simpleSet(elevatorPower.get());
    }
    
    /**
     * Lower the elevator. Power is controlled by a property.
     */
    public void lower(){
        motor.simpleSet(-elevatorPower.get());
    }
    
    public void stop(){
        motor.simpleSet(0);
    }
    
    public void setPower(double power) {
        motor.simpleSet(power);
    }
    
}
    
