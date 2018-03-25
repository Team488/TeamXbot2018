package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logic.VelocityThrottleModule;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class VelocityArcadeDriveCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final PoseSubsystem pose;
    final OperatorInterface oi;
    final HeadingAssistModule ham;
    final VelocityThrottleModule rotationalThrottleModule;
    final VelocityThrottleModule translationalThrottleModule;
    
    final DoubleProperty maxDegreesPerSecondProp;
    final DoubleProperty maxInchesPerSecondProp;

    @Inject
    public VelocityArcadeDriveCommand(
            OperatorInterface oi, 
            DriveSubsystem driveSubsystem, 
            PoseSubsystem pose,
            CommonLibFactory clf,
            XPropertyManager propMan) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.pose = pose;
        this.requires(this.driveSubsystem);
        
        rotationalThrottleModule = 
                clf.createVelocityThrottleModule(getPrefix() + "Rotational", driveSubsystem.getRotationalVelocityPid());
        translationalThrottleModule =
                clf.createVelocityThrottleModule(getPrefix() + "Translational", driveSubsystem.getTranslationalVelocityPid());
        maxDegreesPerSecondProp = propMan.createPersistentProperty(getPrefix() + "MaxDegreesPerSecond", 500);
        maxInchesPerSecondProp = propMan.createPersistentProperty(getPrefix() + "MaxInchesPerSecond", 120);        
        
        HeadingModule holdHeading = clf.createHeadingModule(driveSubsystem.getRotateToHeadingPid());
        HeadingModule decayHeading = clf.createHeadingModule(driveSubsystem.getRotateDecayPid());
        ham = clf.createHeadingAssistModule(holdHeading, decayHeading);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        ham.reset();
    }

    @Override
    public void execute() {
        double translateSpeedGoal = MathUtils.deadband(oi.driverGamepad.getLeftVector().y, 0.05) * maxInchesPerSecondProp.get();
        double rotateSpeedGoal = MathUtils.deadband(oi.driverGamepad.getRightVector().x, 0.05) * maxDegreesPerSecondProp.get();
        
        double rotationalVelocityInDegrees = -pose.getYawAngularVelocity() * 180 / Math.PI;
        //log.info("RotVel: " + rotationalVelocityInDegrees);
        /*double rotationalPower = rotationalThrottleModule.calculateThrottle(
                rotateSpeedGoal, 
                rotationalVelocityInDegrees);*/
        double translationalPower = translationalThrottleModule.calculateThrottle(
                translateSpeedGoal, 
                driveSubsystem.getVelocityInInchesPerSecond());
        
        double turn = ham.calculateHeadingPower(MathUtils.squareAndRetainSign(oi.driverGamepad.getRightVector().x));
        driveSubsystem.drive(new XYPair(0, translationalPower), turn);
    }

}
