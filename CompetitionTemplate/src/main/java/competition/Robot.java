
package competition;

import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import xbot.common.command.BaseRobot;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class Robot extends BaseRobot {

	DoubleProperty example;
	
	@Override
    protected void setupInjectionModule() {
        this.injectionModule = new CompetitionModule();
    }
	
    @Override
    protected void initializeSystems() {
        super.initializeSystems();
        this.injector.getInstance(SubsystemDefaultCommandMap.class);
        this.injector.getInstance(OperatorCommandMap.class);
    }
}
