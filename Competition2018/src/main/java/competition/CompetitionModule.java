package competition;

import competition.subsystems.offboard.OffboardCommsCanInterface;
import competition.subsystems.offboard.XOffboardCommsInterface;
import competition.subsystems.autonomous.GameDataSource;
import competition.subsystems.autonomous.RealGameDataAdapter;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.RobotModule;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class CompetitionModule extends RobotModule {

    boolean isPractice;
    
    public CompetitionModule(boolean isPractice) {
        this.isPractice = isPractice;
    }
    
    @Override
    protected void configure() {
        super.configure();
        this.bind(BasePoseSubsystem.class).to(PoseSubsystem.class);
        this.bind(XOffboardCommsInterface.class).to(OffboardCommsCanInterface.class);
        this.bind(BaseDriveSubsystem.class).to(DriveSubsystem.class);
        this.bind(GameDataSource.class).to(RealGameDataAdapter.class);
        
        if (isPractice) {
            this.bind(ElectricalContract2018.class).to(Practice2018Robot.class);
        } else {
            this.bind(ElectricalContract2018.class).to(Competition2018Robot.class);
        }
    }
}
