package competition.subsystems.drive.commands;
import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDManager;
import xbot.common.math.PIDFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

public class DriveForDistanceCommand extends BaseCommand {
    
    private final PIDManager travelManager;
    private final PIDManager headingDrivePid;
    private final PoseSubsystem poseSubsystem;
    private final DriveSubsystem drive;
    
    private double deltaDistance;
    private double targetDistance;
    private double startingPosition;
    public final double defaultPValue = 1/80d;
    
    private DoubleProperty deltaDistanceProp;
    private ContiguousHeading targetHeading;
    
    @Inject
    public DriveForDistanceCommand(
            DriveSubsystem driveSubsystem,
            XPropertyManager propManager,
            RobotAssertionManager assertionManager,
            PIDFactory pidFactory,
            PoseSubsystem pose) {
        
    	this.drive = driveSubsystem;
        this.poseSubsystem = pose;
        this.requires(driveSubsystem);
        this.travelManager = pidFactory.createPIDManager("Drive to position", 0.1, 0, 0, 0, 0.5, -0.5, 3, 1, 0.5);
        headingDrivePid = pidFactory.createPIDManager("Heading module", defaultPValue, 0, 0);
        targetHeading = new ContiguousHeading();
    }
    
    /**
     * Sets the target distance
     * @param deltaDistance the target in inches
     */
    public void setDeltaDistance(double deltaDistance) {
        this.deltaDistance = deltaDistance;
    }
    
    /**
     * Sets the target distance with a DoubleProperty
     * @param deltaDistanceProp the DoubleProperty for the target
     */
    public void setDeltaDistance(DoubleProperty deltaDistanceProp) {
        this.deltaDistanceProp = deltaDistanceProp;
    }   
    
    @Override
    public void initialize() {
        startingPosition = getYDistance();
        
        targetHeading = poseSubsystem.getCurrentHeading();
        
        if (deltaDistanceProp != null) {
            this.targetDistance = getYDistance() + deltaDistanceProp.get();
        } else {
            this.targetDistance = getYDistance() + deltaDistance;
        }
        log.info("Initializing  with distance " + targetDistance + " inches");
    }

    @Override
    public void execute() {
        double power = travelManager.calculate(targetDistance, getYDistance());
        double headingPower = calculateHeadingPower();
        
        double leftPower = power - headingPower;
        double rightPower = power + headingPower;
        
        drive.drive(leftPower, rightPower);
    }
    
    public double calculateHeadingPower() {
        double errorInDegrees = targetHeading.difference(poseSubsystem.getCurrentHeading());
        double normalizedError = errorInDegrees / 180;
        double rotationalPower = headingDrivePid.calculate(0, normalizedError);

        return rotationalPower;
    }
    
    private double getYDistance() {
        return poseSubsystem.getRobotOrientedTotalDistanceTraveled().y;
    }
    
    @Override
    public boolean isFinished() {
        return  travelManager.isOnTarget();
    }
    
    @Override
    public void end() {
        log.info("Ending, PreviousPosition was " + startingPosition 
                + ", Targeted Delta in distance is " + deltaDistance + " Distance traveled is " 
                + (deltaDistance - startingPosition));
        drive.stop();
    }
}
