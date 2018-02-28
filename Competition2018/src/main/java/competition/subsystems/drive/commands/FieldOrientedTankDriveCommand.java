package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class FieldOrientedTankDriveCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final OperatorInterface oi;
    final HeadingModule headingModule;

    @Inject
    public FieldOrientedTankDriveCommand(OperatorInterface oi, DriveSubsystem driveSubsystem, CommonLibFactory clf) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.requires(this.driveSubsystem);

        headingModule = clf.createHeadingModule(driveSubsystem.getRotateToHeadingPid());
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        headingModule.reset();
    }

    @Override
    public void execute() {
        XYPair driveVector = oi.driverGamepad.getLeftVector();
        
        if (driveVector.getMagnitude() > 0.2) {
            double turnPower = headingModule.calculateHeadingPower(driveVector.getAngle());
            double translatePower = MathUtils.constrainDoubleToRobotScale(driveVector.getMagnitude());
            driveSubsystem.drive(new XYPair(0, translatePower), turnPower);
        } else {
            driveSubsystem.drive(0,0);
        }
    }

}
