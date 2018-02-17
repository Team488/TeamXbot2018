
package competition;

import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseRobot;
import xbot.common.properties.DoubleProperty;

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
<<<<<<< HEAD
        periodicDataSources.add(this.injector.getInstance(OffboardInterfaceSubsystem.class));
        //periodicDataSources.add(this.injector.getInstance(ElevatorSubsystem.class));
=======
        periodicDataSources.add(this.injector.getInstance(ElevatorSubsystem.class));
        periodicDataSources.add(this.injector.getInstance(WristSubsystem.class));
>>>>>>> master
    }
}
