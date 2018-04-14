package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
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
    
    DoubleProperty powerLimit;
    DoubleProperty elevatorHeightLimit;
    
    double currentTranslateOutput;
    double currentRotateOutput;
    
    double translateAdjustedPower;
    double rotateAdjustedPower;
    
    @Inject
    public ArcadeDriveWithJoysticksCommand(OperatorInterface oi, DriveSubsystem driveSubsystem, CommonLibFactory clf,
            ElevatorSubsystem elevatorSubsystem, XPropertyManager propManager) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.elevatorSubsystem = elevatorSubsystem;
        this.requires(this.driveSubsystem);
        
        powerLimit = propManager.createPersistentProperty("Power Limit for low acceleration mode", 0.2);
        elevatorHeightLimit = propManager.createPersistentProperty("Elevator Height Limit for low acceleration mode", 40);

        HeadingModule holdHeading = clf.createHeadingModule(driveSubsystem.getRotateToHeadingPid());
        HeadingModule decayHeading = clf.createHeadingModule(driveSubsystem.getRotateDecayPid());
        ham = clf.createHeadingAssistModule(holdHeading, decayHeading);
    }

    @Override
    public void initialize() {
        log.info("Initializing ArcadeDriveWithJoysticksCommand");
        ham.reset();
        
        currentTranslateOutput = 0;
        currentRotateOutput = 0;
        
        translateAdjustedPower = 0;
        rotateAdjustedPower = 0;
    }

    @Override
    public void execute() {
        double nextTranslateInput = oi.driverGamepad.getLeftVector().y;
        double nextRotateInput = MathUtils.squareAndRetainSign(oi.driverGamepad.getRightVector().x);
        double turn = ham.calculateHeadingPower(nextRotateInput);
        
        double translateInputDelta = nextTranslateInput - currentTranslateOutput;
        double rotateInputDelta = nextRotateInput - currentRotateOutput;
        
        // Rotate
        if (elevatorSubsystem.getCurrentHeightInInches() > elevatorHeightLimit.get()) {
            if (rotateInputDelta > 0.2) {
                rotateAdjustedPower += 0.2;
                turn = ham.calculateHeadingPower(rotateAdjustedPower);
                currentRotateOutput = rotateAdjustedPower;
            }
            else if (rotateInputDelta < -0.2) {
                rotateAdjustedPower += -0.2;
                turn = ham.calculateHeadingPower(rotateAdjustedPower);
                currentRotateOutput = rotateAdjustedPower;
            }
            else {
                currentRotateOutput = nextRotateInput;
            }
        } else {
            currentRotateOutput = nextRotateInput;
        }
        
        // Translate
        if (elevatorSubsystem.getCurrentHeightInInches() > elevatorHeightLimit.get()) {
            if (translateInputDelta > 0.2) {
                translateAdjustedPower += 0.2;
                driveSubsystem.drive(new XYPair(0, translateAdjustedPower), turn);
                currentTranslateOutput = translateAdjustedPower;
            }
            else if(translateInputDelta < -0.2) {
                translateAdjustedPower += -0.2;
                driveSubsystem.drive(new XYPair(0, translateAdjustedPower), turn);
                currentTranslateOutput = translateAdjustedPower;
            }
            else {
            driveSubsystem.drive(new XYPair(0, nextTranslateInput), turn);
            currentTranslateOutput = nextTranslateInput;
            }
        }
        else {
            driveSubsystem.drive(new XYPair(0, nextTranslateInput), turn);
            currentTranslateOutput = nextTranslateInput;
            currentRotateOutput = nextRotateInput;
        }
    }
}
