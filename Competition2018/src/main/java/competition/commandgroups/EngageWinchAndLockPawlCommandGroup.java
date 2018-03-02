package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.EngagePawlCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class EngageWinchAndLockPawlCommandGroup extends CommandGroup {

    @Inject
    public EngageWinchAndLockPawlCommandGroup(EngagePawlCommand engage, AscendClimberCommand ascend) {
        this.addSequential(engage);
        this.addSequential(ascend);
    }

}
