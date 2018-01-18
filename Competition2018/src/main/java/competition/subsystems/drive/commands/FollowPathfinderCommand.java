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
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class FollowPathfinderCommand extends BaseCommand {

    DriveSubsystem drive;
    TankModifier tankPaths;
    final HeadingModule heading;
    
    final EncoderFollower leftFollower;
    final EncoderFollower rightFollower;
    
    double leftOffset;
    double rightOffset;
    
    final DoubleProperty wheelDiameterInFeet;
    final DoubleProperty driveBaseWidthInFeet;
    final DoubleProperty encoderTicksPerRev;
    final DoubleProperty maxVelocity;
    
    
    @Inject
    public FollowPathfinderCommand(CommonLibFactory clf, PIDFactory pf, DriveSubsystem drive, XPropertyManager propMan) {
        this.drive = drive;
        this.requires(drive);
        
        heading = clf.createHeadingModule(pf.createPIDManager("PathfinderHeading", 0.05, 0, 0));
        
        leftFollower = new EncoderFollower();
        rightFollower = new EncoderFollower();
        
        wheelDiameterInFeet = propMan.createPersistentProperty("wheelDiameterInFeet", 0.3333);
        driveBaseWidthInFeet = propMan.createPersistentProperty("DriveBaseWidthInFeet", 2.0);
        encoderTicksPerRev = propMan.createPersistentProperty("EncoderTicksPerRev", 360);
        maxVelocity = propMan.createPersistentProperty("maxVelocityInFeet", 8);
    }
    
    @Override
    public void initialize() {
        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(5, 0, 0)   
            };
            
            Trajectory.Config config = new Trajectory.Config(
                    Trajectory.FitMethod.HERMITE_CUBIC, 
                    Trajectory.Config.SAMPLES_HIGH, 
                    0.02, // delta time. 0.02 is 50Hz, which is roughly our control loop.
                    4.0,  // max velocity
                    2.0,  // max acceleration
                    60.0 // max jerk
                    );
            Trajectory trajectory = Pathfinder.generate(points, config);  
            
            tankPaths = new TankModifier(trajectory).modify(driveBaseWidthInFeet.get());
            
            leftFollower.setTrajectory(tankPaths.getLeftTrajectory());
            rightFollower.setTrajectory(tankPaths.getRightTrajectory());
            
            leftFollower.configurePIDVA(1, 0, 0, 1 / maxVelocity.get(), 0);
            rightFollower.configurePIDVA(1, 0, 0, 1 / maxVelocity.get(), 0);
                        
            leftFollower.configureEncoder(
                    drive.getLeftRawTotalDistance(),
                    (int)encoderTicksPerRev.get(), 
                    wheelDiameterInFeet.get());
            
            rightFollower.configureEncoder(
                    drive.getRightRawTotalDistance(),
                    (int)encoderTicksPerRev.get(), 
                    wheelDiameterInFeet.get());
    }

    @Override
    public void execute() {
        double leftPower = leftFollower.calculate(drive.getLeftRawTotalDistance());
        double rightPower = rightFollower.calculate(drive.getRightRawTotalDistance());
        
        double turnPower = heading.calculateHeadingPower(Pathfinder.r2d(leftFollower.getHeading()));
                
        drive.drive(leftPower-turnPower, rightPower+turnPower);
    }
    
    @Override
    public boolean isFinished() {
        return leftFollower.isFinished() || rightFollower.isFinished();
    }
}
