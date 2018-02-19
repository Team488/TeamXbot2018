package competition.subsystems.autonomous.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class DriveNowhereCommand extends BaseCommand {

    DriveSubsystem drive;

    @Inject
    public DriveNowhereCommand(DriveSubsystem driveSubsystem) {
        this.requires(driveSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        drive.stop();
    }

}
