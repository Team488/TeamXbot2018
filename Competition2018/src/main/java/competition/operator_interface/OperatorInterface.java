package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.controls.sensors.XFTCGamepad;
import xbot.common.controls.sensors.XJoystick;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logging.RobotAssertionManager;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands and command groups
 * that allow control of the robot.
 */
@Singleton
public class OperatorInterface {
    public XFTCGamepad gamepad;

    @Inject
    public OperatorInterface(CommonLibFactory factory, RobotAssertionManager assertionManager) {
        gamepad = factory.createGamepad(3, 10);

        gamepad.setLeftStickYInversion(true);
        gamepad.setRightStickYInversion(true);
    }
}
