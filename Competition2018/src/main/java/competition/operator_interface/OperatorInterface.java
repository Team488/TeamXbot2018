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

    public final XFTCGamepad driverGamepad;
    public final XFTCGamepad operatorGamepad;
    public final XFTCGamepad programmerGamepad;

    public final AnalogHIDDescription gripperIntake;
    public final AnalogHIDDescription gripperEject;
    public final AnalogHIDDescription raiseClimber;
    public final AnalogHIDDescription lowerClimber;

    @Inject
    public OperatorInterface(CommonLibFactory factory, RobotAssertionManager assertionManager) {
        driverGamepad = factory.createGamepad(0, 10);
        driverGamepad.setLeftStickYInversion(true);
        driverGamepad.setRightStickYInversion(true);
        driverGamepad.setRightStickXInversion(true);

        operatorGamepad = factory.createGamepad(1, 10);
        operatorGamepad.setLeftStickYInversion(true);
        operatorGamepad.setRightStickYInversion(true);
        
        programmerGamepad = factory.createGamepad(2, 10);
        programmerGamepad.setLeftStickYInversion(true);
        programmerGamepad.setRightStickYInversion(true);        

        gripperIntake = new AnalogHIDDescription(3, .75, 1.0);
        operatorGamepad.addAnalogButton(gripperIntake);

        gripperEject = new AnalogHIDDescription(2, .75, 1.0);
        operatorGamepad.addAnalogButton(gripperEject);

        raiseClimber = new AnalogHIDDescription(3, .75, 1.0);
        driverGamepad.addAnalogButton(raiseClimber);

        lowerClimber = new AnalogHIDDescription(2, .75, 1.0);
        driverGamepad.addAnalogButton(lowerClimber);
    }
}
