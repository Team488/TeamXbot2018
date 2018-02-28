package competition.subsystems.autonomous.commands;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import xbot.common.command.BaseCommand;

public class ChangeAutoDelayCommand extends BaseCommand{

    AutonomousDecisionSystem decider;
    double delayChangeAmount;
    
    @Inject
    public ChangeAutoDelayCommand(AutonomousDecisionSystem decider) {
        this.decider = decider;
        delayChangeAmount = 1;
        this.setRunWhenDisabled(true);
    }
    
    public void setDelayChangeAmount(double delayChangeAmount) {
        this.delayChangeAmount = delayChangeAmount;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        decider.changeAutoDelay(delayChangeAmount);
    }

    @Override
    public void execute() {        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
    
}
