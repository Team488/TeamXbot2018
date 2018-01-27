package competition.subsystems.drive.commands;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public abstract class BaseDriveCommand extends BaseCommand {

    protected final DriveSubsystem driveSubsystem;
    
    public BaseDriveCommand(DriveSubsystem driveSubsystem) {
        this.driveSubsystem = driveSubsystem; 
        this.requires(driveSubsystem);
    }
}
