package competition.subsystems.autonomous;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class DriveNowhereCommand extends BaseCommand {
    
    DriveSubsystem drive;
    
    public DriveNowhereCommand(DriveSubsystem driveSubsystem) {
        this.requires(driveSubsystem);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        drive.rightMaster.simpleSet(0);
        drive.leftMaster.simpleSet(0);
        drive.rightFollower.simpleSet(0);
        drive.leftFollower.simpleSet(0);
    }
    
}
