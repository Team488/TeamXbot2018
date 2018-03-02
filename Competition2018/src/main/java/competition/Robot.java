
package competition;

import java.io.File;

import org.apache.log4j.Logger;

import competition.operator_interface.OperatorCommandMap;
import competition.subsystems.SubsystemDefaultCommandMap;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.power_state_manager.PowerStateManagerSubsystem;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseRobot;

public class Robot extends BaseRobot {

    static Logger log = Logger.getLogger(Robot.class);

    @Override
    protected void setupInjectionModule() {
        this.injectionModule = new CompetitionModule(isPracticeRobot());
    }
    
    private boolean isPracticeRobot() {
        File practiceRobotFlag = new File("/home/lvuser/practicerobot.txt");
        return practiceRobotFlag.exists();
    }

    @Override
    protected void initializeSystems() {
        super.initializeSystems();
        
        Logger log = Logger.getLogger(Robot.class);
        log.info("Am I a practice robot? " + isPracticeRobot());
        
        this.injector.getInstance(SubsystemDefaultCommandMap.class);
        this.injector.getInstance(OperatorCommandMap.class);
        ElectricalContract2018 contract = this.injector.getInstance(ElectricalContract2018.class);

        registerPeriodicDataSource(this.injector.getInstance(PoseSubsystem.class));
        registerPeriodicDataSource(this.injector.getInstance(OffboardInterfaceSubsystem.class));
        registerPeriodicDataSource(this.injector.getInstance(PowerStateManagerSubsystem.class));
        
        registerPeriodicDataSource(this.injector.getInstance(DriveSubsystem.class));
        if (contract.elevatorReady()) {
            registerPeriodicDataSource(this.injector.getInstance(ElevatorSubsystem.class)); 
        }        
        if (contract.wristReady()) {
            registerPeriodicDataSource(this.injector.getInstance(WristSubsystem.class));
        }
    }
}
