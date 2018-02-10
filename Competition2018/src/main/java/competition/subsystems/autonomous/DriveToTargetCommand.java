package competition.subsystems.autonomous;

import competition.subsystems.drive.commands.DriveForDistanceCommand;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;

public class DriveToTargetCommand extends BaseCommand {

    DoubleProperty deltaDistance;
    
    DriveForDistanceCommand driveForDistance;

    @Override
    public void initialize() {
        /**
         * deltaDistance is in inches
         */
        driveForDistance.setDeltaDistance(deltaDistance.get());
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
