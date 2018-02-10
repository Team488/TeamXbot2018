package competition.subsystems.autonomous;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import xbot.common.command.BaseCommand;

public class DriveNowhereCommand extends BaseCommand {

    DriveForDistanceCommand driveForDistance;

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return driveForDistance.isFinished();
    }

    @Override
    public void end() {
        driveForDistance.end();
    }
}
