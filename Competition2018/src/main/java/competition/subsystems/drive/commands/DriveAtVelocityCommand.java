package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.logging.RobotAssertionManager;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.PIDManager;
import xbot.common.math.PIDFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.control_logic.HeadingModule;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;

public class DriveAtVelocityCommand extends BaseCommand {
    private final DriveSubsystem drive;

    @Inject
    public DriveAtVelocityCommand(DriveSubsystem drive, XPropertyManager propManager) {
        this.drive = drive;
        requires(drive);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        drive.driveTankVelocity(50, 50);
    }
}
