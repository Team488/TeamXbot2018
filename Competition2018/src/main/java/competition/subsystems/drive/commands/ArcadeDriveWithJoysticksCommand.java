package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class ArcadeDriveWithJoysticksCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final ElevatorSubsystem elevatorSubsystem;
    final OperatorInterface oi;
    final HeadingAssistModule ham;
    
    DoubleProperty secondsToFullPowerProp;
    DoubleProperty elevatorHeightLimitProp;
    
    double lastTranslatePower;
    double lastRotatePower;
    double lastTime;
    
    @Inject
    public ArcadeDriveWithJoysticksCommand(OperatorInterface oi, DriveSubsystem driveSubsystem, CommonLibFactory clf,
            ElevatorSubsystem elevatorSubsystem, XPropertyManager propManager) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.elevatorSubsystem = elevatorSubsystem;
        this.requires(this.driveSubsystem);
        
        secondsToFullPowerProp = propManager.createPersistentProperty(getPrefix() + "LowAccel: Seconds to full power", 1);
        elevatorHeightLimitProp = propManager.createPersistentProperty("LowAccel: Elevator Height Trigger", 40);

        HeadingModule holdHeading = clf.createHeadingModule(driveSubsystem.getRotateToHeadingPid());
        HeadingModule decayHeading = clf.createHeadingModule(driveSubsystem.getRotateDecayPid());
        ham = clf.createHeadingAssistModule(holdHeading, decayHeading);
    }

    @Override
    public void initialize() {
        log.info("Initializing ArcadeDriveWithJoysticksCommand");
        ham.reset();
        
        lastTranslatePower = 0;
        lastRotatePower = 0;
    }
    
    public double getSecondsToFullPower() {
        return secondsToFullPowerProp.get();
    }

    @Override
    public void execute() {
        double currentTime = Timer.getFPGATimestamp();
        double timeDelta = currentTime - lastTime;        
        
        double humanTranslateInput = oi.driverGamepad.getLeftVector().y;
        double humanRotateInput = MathUtils.squareAndRetainSign(oi.driverGamepad.getRightVector().x);
        double turn = ham.calculateHeadingPower(humanRotateInput);
        
        double translationPower = humanTranslateInput;
        double rotationPower = turn;
        
        if (elevatorSubsystem.getCurrentHeightInInches() > elevatorHeightLimitProp.get()) {
            double timeFactor = 1;
            if (secondsToFullPowerProp.get() != 0) {
                timeFactor = timeDelta / secondsToFullPowerProp.get();
            }
            
            translationPower = MathUtils.constrainDouble(translationPower, lastTranslatePower-timeFactor, lastTranslatePower+timeFactor);
            rotationPower = MathUtils.constrainDouble(rotationPower, lastRotatePower-timeFactor, lastRotatePower+timeFactor);
        }
        
        driveSubsystem.drive(new XYPair(0, translationPower), rotationPower);
        lastTranslatePower = translationPower;
        lastRotatePower = rotationPower;
        lastTime = currentTime;
    }
}
