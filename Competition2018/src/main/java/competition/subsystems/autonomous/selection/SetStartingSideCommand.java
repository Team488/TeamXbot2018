package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import xbot.common.command.BaseCommand;

public class SetStartingSideCommand extends BaseCommand {

    AutonomousDecisionSystem decider;
    boolean isRightSide = true;
    
    @Inject
    public SetStartingSideCommand(AutonomousDecisionSystem decider) {
        this.decider = decider;
        this.setRunWhenDisabled(true);
    }
    
    public void setRightSide(boolean robotOnRightSide) {
        isRightSide = robotOnRightSide;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing with isRightSide:" + isRightSide);
        decider.setRobotPosition(isRightSide);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
