package competition.subsystems.offboard.commands;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.google.inject.Inject;

import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.data.TargetCubeInfo;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.drive.PurePursuitCommand;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class PurePursuitToVisibleCubeCommand extends PurePursuitCommand {

    private final OffboardInterfaceSubsystem offboardSubsystem;
    private final PoseSubsystem poseSubsystem;
    //private final ZedDeploySubsystem zedDeploy;
    
    private Supplier<TargetCubeInfo> targetSupplier;
    
    @Inject
    public PurePursuitToVisibleCubeCommand(
            CommonLibFactory clf,
            BasePoseSubsystem pose,
            BaseDriveSubsystem drive,
            OffboardInterfaceSubsystem offboardSubsystem,
            PoseSubsystem poseSubsystem,
            //ZedDeploySubsystem zedDeploy,
            XPropertyManager propMan) {
        super(clf, pose, drive, propMan);
        this.offboardSubsystem = offboardSubsystem;
        this.poseSubsystem = poseSubsystem;
        //this.zedDeploy = zedDeploy;
        
       // this.requires(zedDeploy);
    }
    
    public void setTargetCubeSupplier(Supplier<TargetCubeInfo> targetSupplier) {
        this.targetSupplier = targetSupplier;
    }
    
    @Override
    public void initialize() {
        //zedDeploy.setIsExtended(true);
        super.initialize();
    }
    
    @Override
    protected List<RabbitPoint> getOriginalPoints() {
        TargetCubeInfo targetCube = targetSupplier == null ? offboardSubsystem.getTargetCube() : targetSupplier.get();
        if (targetCube == null) {
            return null;
        }
        double headingDelta = Math.toDegrees(Math.atan2(targetCube.xInches, targetCube.yInches));

        FieldPose targetPose = new FieldPose(new XYPair(targetCube.xInches, targetCube.yInches),
                new ContiguousHeading(90 - headingDelta));
        
        return Arrays.asList(new RabbitPoint(targetPose));
    }

    @Override
    protected PointLoadingMode getPursuitMode() {
        return PointLoadingMode.Relative;
    }

}
