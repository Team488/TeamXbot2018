package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousPathSupplier;
import competition.subsystems.autonomous.AutonomousPathSupplier.StartingLocations;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;

public class SetStartingSideCommand extends BaseCommand {

    final AutonomousPathSupplier decider;
    StartingLocations whereToStart;
    final PoseSubsystem pose;
    
    @Inject
    public SetStartingSideCommand(AutonomousPathSupplier decider, PoseSubsystem pose) {
        this.decider = decider;
        this.pose = pose;
        this.setRunWhenDisabled(true);
    }
    
    public void setStartingLocation(StartingLocations whereToStart) {
        this.whereToStart = whereToStart;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing with position:" + whereToStart);
        decider.setRobotPosition(whereToStart);
        
        switch (whereToStart) {
        case Left:
            pose.setCurrentPosition(4 * 12, 3.1 * 12);
            break;
        case Middle:
            pose.setCurrentPosition(15, 3.1 * 12);
            break;
        case Right:
            pose.setCurrentPosition(23, 3.1 * 12);
            break;
        default: 
            break;
        }
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
