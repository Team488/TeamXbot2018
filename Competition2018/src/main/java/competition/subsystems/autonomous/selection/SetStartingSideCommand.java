package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import competition.subsystems.autonomous.AutonomousDecisionSystem.StartingLocations;
import xbot.common.command.BaseCommand;

public class SetStartingSideCommand extends BaseCommand {

    AutonomousDecisionSystem decider;
    StartingLocations whereToStart;
    
    @Inject
    public SetStartingSideCommand(AutonomousDecisionSystem decider) {
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
