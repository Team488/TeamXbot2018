package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class ArcadeDriveWithJoysticksCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final OperatorInterface oi;
    final HeadingAssistModule ham;

    @Inject
    public ArcadeDriveWithJoysticksCommand(OperatorInterface oi, DriveSubsystem driveSubsystem, CommonLibFactory clf) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.requires(this.driveSubsystem);

        HeadingModule holdHeading = clf.createHeadingModule(driveSubsystem.getRotateToHeadingPid());
        HeadingModule decayHeading = clf.createHeadingModule(driveSubsystem.getRotateDecayPid());
        ham = clf.createHeadingAssistModule(holdHeading, decayHeading);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {

        double left = oi.driverGamepad.getLeftVector().y;
        double right = oi.driverGamepad.getLeftVector().x;
        double yTranslate = (left+right) / 2;
        double turn = ham.calculateHeadingPower((right-left)/2);
        driveSubsystem.drive(new XYPair(0, yTranslate), turn);
    }

}
