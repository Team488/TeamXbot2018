package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import xbot.common.command.BaseCommandGroup;
import xbot.common.subsystems.drive.PurePursuitCommand;

public class DynamicScoreOnSwitchCommandGroup extends BaseCommandGroup {

    @Inject
    public DynamicScoreOnSwitchCommandGroup(AutonomousDecisionSystem decider,
            PurePursuitCommand pursuit) {
        
        pursuit.setPointSupplier(decider.getAutoPath());
    }
}
