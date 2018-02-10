package competition.subsystems.autonomous;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import xbot.common.command.BaseCommand;

public class Drive5FtForwardCommand extends BaseCommand {

    DriveForDistanceCommand driveForDistance;

    @Override
    public void initialize() {
        /**
         * 60 is the target distance in inches
         */
        driveForDistance.setDeltaDistance(60);
        driveForDistance.initialize();
    }

    @Override
    public void execute() {
        driveForDistance.execute();
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
