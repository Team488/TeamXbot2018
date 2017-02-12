package competition;

import org.junit.Test;

import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import xbot.common.injection.BaseWPITest;

public class RobotInitTest extends BaseWPITest {
    @Test
    public void testDefaultSystem() {
        this.injector.getInstance(SubsystemDefaultCommandMap.class);
        this.injector.getInstance(OperatorCommandMap.class);
    }
}