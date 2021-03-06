package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousPathSupplier;
import competition.subsystems.autonomous.AutonomousPathSupplier.StartingLocations;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.command.BaseCommand;

public class SetStartingSideCommand extends BaseCommand {

    final AutonomousPathSupplier decider;
    StartingLocations whereToStart;
    
    @Inject
    public SetStartingSideCommand(AutonomousPathSupplier decider) {
        this.decider = decider;
        this.setRunWhenDisabled(true);
    }
    
    public void setStartingLocation(StartingLocations whereToStart) {
        this.whereToStart = whereToStart;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing with position:" + whereToStart);
        decider.setRobotPosition(whereToStart);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
