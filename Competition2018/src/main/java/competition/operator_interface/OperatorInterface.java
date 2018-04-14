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

    public final AnalogHIDDescription operatorGripperIntake;
    public final AnalogHIDDescription operatorGripperEject;
    public final AnalogHIDDescription driverRightTrigger;
    public final AnalogHIDDescription driverLeftTrigger;

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

        operatorGripperIntake = new AnalogHIDDescription(3, .75, 1.0);
        operatorGamepad.addAnalogButton(operatorGripperIntake);

        operatorGripperEject = new AnalogHIDDescription(2, .75, 1.0);
        operatorGamepad.addAnalogButton(operatorGripperEject);

        driverRightTrigger = new AnalogHIDDescription(3, .75, 1.0);
        driverGamepad.addAnalogButton(driverRightTrigger);

        driverLeftTrigger = new AnalogHIDDescription(2, .75, 1.0);
        driverGamepad.addAnalogButton(driverLeftTrigger);
    }
}
