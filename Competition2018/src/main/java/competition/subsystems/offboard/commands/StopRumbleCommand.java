package competition.subsystems.offboard.commands;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.google.inject.Inject;

import competition.operator_interface.RumbleManager;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.data.TargetCubeInfo;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.zed_deploy.ZedDeploySubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.drive.PurePursuitCommand;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class StopRumbleCommand extends BaseCommand {
    private final RumbleManager rumbleManager;
    
    @Inject
    public StopRumbleCommand(RumbleManager rumbleManager) {
        this.rumbleManager = rumbleManager;
    }
    
    @Override
    public void initialize() {
        rumbleManager.stopDriverGamepadRumble();
    }
    
    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }

}
