package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class ArcadeDriveWithJoysticksCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final ElevatorSubsystem elevatorSubsystem;
    final OperatorInterface oi;
    final HeadingAssistModule ham;
    
    private double translateAdjustedPower;
    private double rotateAdjustedPower;
    private double turn;
    
    private double nextTranslateInput;
    private double currentTranslateOutput;
    private double translateInputDelta;
    
    private double nextRotateInput;
    private double currentRotateOutput;
    private double rotateInputDelta;

    @Inject
    public ArcadeDriveWithJoysticksCommand(
            OperatorInterface oi, DriveSubsystem driveSubsystem, CommonLibFactory clf,
            ElevatorSubsystem elevatorSubsystem) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.elevatorSubsystem = elevatorSubsystem;
        this.requires(this.driveSubsystem);
        
        this.translateAdjustedPower = 0;
        this.rotateAdjustedPower = 0;

        HeadingModule holdHeading = clf.createHeadingModule(driveSubsystem.getRotateToHeadingPid());
        HeadingModule decayHeading = clf.createHeadingModule(driveSubsystem.getRotateDecayPid());
        ham = clf.createHeadingAssistModule(holdHeading, decayHeading);
    }

    @Override
    public void initialize() {
        log.info("Initializing ArcadeDriveWithJoysticksCommand");
        ham.reset();
    }

    @Override
    public void execute() {
        nextTranslateInput = oi.driverGamepad.getLeftVector().y;
        nextRotateInput = MathUtils.squareAndRetainSign(oi.driverGamepad.getRightVector().x);
        
        translateInputDelta = nextTranslateInput - currentTranslateOutput;
        rotateInputDelta = nextRotateInput - currentRotateOutput;
        
        // Rotate
        if (rotateInputDelta > 0.2 && elevatorSubsystem.getCurrentHeightInInches() > 40) {
            rotateAdjustedPower += 0.2;
            turn = ham.calculateHeadingPower(rotateAdjustedPower);
        }
        else if (rotateInputDelta < -0.2 && elevatorSubsystem.getCurrentHeightInInches() > 40) {
            rotateAdjustedPower += -0.2;
            turn = ham.calculateHeadingPower(rotateAdjustedPower);
        }
        else {
            turn = ham.calculateHeadingPower(nextRotateInput);
        }
        
        // Translate
        if (translateInputDelta > 0.2 && elevatorSubsystem.getCurrentHeightInInches() > 40) {
            translateAdjustedPower += 0.2;
            driveSubsystem.drive(new XYPair(0, translateAdjustedPower), turn);
        }
        else if(translateInputDelta < -0.2 && elevatorSubsystem.getCurrentHeightInInches() > 40) {
            translateAdjustedPower += -0.2;
            driveSubsystem.drive(new XYPair(0, translateAdjustedPower), turn);
        }
        else {
        driveSubsystem.drive(new XYPair(0, nextTranslateInput), turn);
        }
    }

}
