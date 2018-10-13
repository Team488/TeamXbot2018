package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class ToggleArcadeDriveCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final ArcadeDriveWithJoysticksCommand arcade;
    final OutreachDriveWithJoysticksCommand outreach;

    @Inject
    public ToggleArcadeDriveCommand(DriveSubsystem driveSubsystem, ArcadeDriveWithJoysticksCommand arcade,
            OutreachDriveWithJoysticksCommand outreach) {
        this.driveSubsystem = driveSubsystem;
        this.arcade = arcade;
        this.outreach = outreach;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        driveSubsystem.setDefaultCommand(arcade);
    }
}
