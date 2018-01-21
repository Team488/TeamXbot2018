
package competition;

import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
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
        
        
        periodicDataSources.add(this.injector.getInstance(DriveSubsystem.class));
        periodicDataSources.add(this.injector.getInstance(PoseSubsystem.class));
    }
}
