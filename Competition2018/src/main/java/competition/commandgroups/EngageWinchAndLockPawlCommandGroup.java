package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.EngagePawlCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.command.BaseCommandGroup;

public class EngageWinchAndLockPawlCommandGroup extends BaseCommandGroup {

    @Inject
    public EngageWinchAndLockPawlCommandGroup(EngagePawlCommand engage, AscendClimberCommand ascend) {
        this.addSequential(engage);
        this.addSequential(ascend);
    }

}
