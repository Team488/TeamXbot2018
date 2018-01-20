package competition;

import competition.subsystems.offboard.OffboardCommsCanInterface;
import competition.subsystems.offboard.XOffboardCommsInterface;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.RobotModule;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class CompetitionModule extends RobotModule {

    @Override
    protected void configure() {
        super.configure();
        this.bind(BasePoseSubsystem.class).to(PoseSubsystem.class);
        this.bind(XOffboardCommsInterface.class).to(OffboardCommsCanInterface.class);
    }
}
