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
import xbot.common.math.MathUtils;
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
    final DoubleProperty pathfinderP;
    
    
    @Inject
    public FollowPathfinderCommand(CommonLibFactory clf, PIDFactory pf, DriveSubsystem drive, XPropertyManager propMan) {
        this.drive = drive;
        this.requires(drive);
        
        heading = clf.createHeadingModule(pf.createPIDManager("PathfinderHeading", 0.05, 0, 0));
        
        leftFollower = new EncoderFollower();
        rightFollower = new EncoderFollower();
        
        wheelDiameterInFeet = propMan.createPersistentProperty("wheelDiameterInFeet", 0.3333);
        driveBaseWidthInFeet = propMan.createPersistentProperty("DriveBaseWidthInFeet", 2.0);
        encoderTicksPerRev = propMan.createPersistentProperty("EncoderTicksPerRev", 220);
        maxVelocity = propMan.createPersistentProperty("maxVelocityInFeet", 6.33);
        pathfinderP = propMan.createPersistentProperty("PathfinderP", 1.0);
    }
    
    @Override
    public void initialize() {
    	
    	log.info("Initializing");
    	
        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(5, 5, Pathfinder.d2r(90))   
            };
            
        Trajectory.Config config = new Trajectory.Config(
                Trajectory.FitMethod.HERMITE_CUBIC, 
                Trajectory.Config.SAMPLES_HIGH, 
                0.02, // delta time. 0.02 is 50Hz, which is roughly our control loop.
                10.0,  // max velocity
                10.0,  // max acceleration
                60.0 // max jerk
                );
        
        log.info("Trajectory Configured");
        
        Trajectory trajectory = Pathfinder.generate(points, config);  
        
        log.info("Trajectory Generated");
        log.info("DB Width: " + driveBaseWidthInFeet.get());
        tankPaths = new TankModifier(trajectory).modify(driveBaseWidthInFeet.get());
        
        leftFollower.setTrajectory(tankPaths.getLeftTrajectory());
        rightFollower.setTrajectory(tankPaths.getRightTrajectory());
        
        log.info("PathfinderP: " + pathfinderP.get());
        log.info("KV: " + 1.0 / maxVelocity.get());
        
        leftFollower.configurePIDVA(pathfinderP.get(), 0, 0, 1.0 / maxVelocity.get(), 0);
        rightFollower.configurePIDVA(pathfinderP.get(), 0, 0, 1.0 / maxVelocity.get(), 0);
                    
        log.info("Starting encoder pos: " + drive.getLeftRawTotalDistance());
        log.info("Encoder revs: " + (int)encoderTicksPerRev.get());
        log.info("Wheel diameter: " + wheelDiameterInFeet.get());
        
        leftFollower.configureEncoder(
                drive.getLeftRawTotalDistance(),
                (int)encoderTicksPerRev.get(), 
                wheelDiameterInFeet.get());
        
        rightFollower.configureEncoder(
                drive.getRightRawTotalDistance(),
                (int)encoderTicksPerRev.get(), 
                wheelDiameterInFeet.get());
        
        log.info("All configuration finished.");
    }

    @Override
    public void execute() {
        double leftPower = leftFollower.calculate(drive.getLeftRawTotalDistance());
        double rightPower = rightFollower.calculate(drive.getRightRawTotalDistance());
        
        //log.info("LeftPos: " + drive.getLeftRawTotalDistance() + ", LeftPower:" + leftPower + " ");
        
        double turnPower = heading.calculateHeadingPower(Pathfinder.r2d(leftFollower.getHeading())+90);
        
        
        leftPower = MathUtils.constrainDoubleToRobotScale(leftPower);
        rightPower = MathUtils.constrainDoubleToRobotScale(rightPower);
        turnPower = MathUtils.constrainDoubleToRobotScale(turnPower);
        log.info(turnPower + ", " + leftPower);
        
        drive.drive(leftPower-turnPower, rightPower+turnPower);
    }
    
    @Override
    public boolean isFinished() {
        return leftFollower.isFinished() || rightFollower.isFinished();
    }
}
