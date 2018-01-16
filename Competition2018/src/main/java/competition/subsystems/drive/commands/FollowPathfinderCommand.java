package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class FollowPathfinderCommand extends BaseCommand {

    DriveSubsystem drive;
    TankModifier tankPaths;
    final HeadingModule heading;
    
    final EncoderFollower leftFollower;
    final EncoderFollower rightFollower;
    
    double leftOffset;
    double rightOffset;
    
    
    @Inject
    public FollowPathfinderCommand(CommonLibFactory clf, PIDFactory pf, DriveSubsystem drive) {
        this.drive = drive;
        this.requires(drive);
        
        heading = clf.createHeadingModule(pf.createPIDManager("PathfinderHeading", 0.05, 0, 0));
        
        leftFollower = new EncoderFollower();
        rightFollower = new EncoderFollower();
    }
    
    @Override
    public void initialize() {
        Waypoint[] points = new Waypoint[] {
                new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
                new Waypoint(0, 0, 0)    
            };
            
            Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.02, 1.7, 2.0, 60.0);
            Trajectory trajectory = Pathfinder.generate(points, config);  
            
            tankPaths = new TankModifier(trajectory).modify(0.5);
            
            leftFollower.setTrajectory(tankPaths.getLeftTrajectory());
            rightFollower.setTrajectory(tankPaths.getRightTrajectory());
            
            leftOffset = drive.getLeftTotalDistance();
            rightOffset = drive.getRightTotalDistance();
            
            leftFollower.configureEncoder(
                    drive.getLeftRawTotalDistance(),
                    360, 
                    4);
            
            rightFollower.configureEncoder(
                    drive.getRightRawTotalDistance(),
                    360,
                    4);
    }

    @Override
    public void execute() {
        
        
        double turnPower = heading.calculateHeadingPower(desiredHeading)
    }

}
