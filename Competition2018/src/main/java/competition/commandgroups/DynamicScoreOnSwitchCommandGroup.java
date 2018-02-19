package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.PurePursuitCommand;

public class DynamicScoreOnSwitchCommandGroup extends BaseCommandGroup {

    public PurePursuitCommand pursuit;
    
    @Inject
    public DynamicScoreOnSwitchCommandGroup(AutonomousDecisionSystem decider,
            PurePursuitCommand pursuit) {
        this.pursuit = pursuit;
        pursuit.setPointSupplier(decider.getAutoPath());
        
        this.addSequential(pursuit);
    }
}
