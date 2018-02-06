package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.controls.sensors.AnalogHIDButton.AnalogHIDDescription;
import xbot.common.controls.sensors.XFTCGamepad;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logging.RobotAssertionManager;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands and command groups
 * that allow control of the robot.
 */
@Singleton
public class OperatorInterface {

    public XFTCGamepad driverGamepad;
    public XFTCGamepad operatorGamepad;
    
    /*public final AnalogHIDDescription raiseElevator;
    public final AnalogHIDDescription lowerElevator;
    public final AnalogHIDDescription raiseClimber;
    public final AnalogHIDDescription lowerClimber;*/

    @Inject
    public OperatorInterface(CommonLibFactory factory, RobotAssertionManager assertionManager) {
        driverGamepad = factory.createGamepad(3, 10);

        driverGamepad.setLeftStickYInversion(true);
        driverGamepad.setRightStickYInversion(true);

        operatorGamepad = factory.createGamepad(0, 10);

        operatorGamepad.setLeftStickYInversion(true);
        operatorGamepad.setRightStickYInversion(true);
        
        /*raiseElevator = new AnalogHIDDescription(3, .501, 1.0);
        operatorGamepad.addAnalogButton(raiseElevator);
        
        lowerElevator = new AnalogHIDDescription(2, .501, 1.0);
        operatorGamepad.addAnalogButton(lowerElevator);
        
        raiseClimber = new AnalogHIDDescription(3, .501, 1.0);
        driverGamepad.addAnalogButton(raiseClimber);
        
        lowerClimber = new AnalogHIDDescription(2, .501, 1.0);
        driverGamepad.addAnalogButton(lowerClimber);*/
    }
}
