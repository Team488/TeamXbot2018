package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.AscendLowPowerCommand;
import competition.subsystems.climb.commands.DescendClimberCommand;
import competition.subsystems.climb.commands.ReleasePawlCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import xbot.common.command.BaseCommandGroup;

public class DisengageWinchAndReleasePawlCommandGroup extends BaseCommandGroup {
    
    @Inject
    public DisengageWinchAndReleasePawlCommandGroup(
            AscendLowPowerCommand ascendLowPower,
            ReleasePawlCommand release,
            DescendClimberCommand decend) 
    {
        this.addSequential(release);
        this.addSequential(ascendLowPower, 0.05);
        this.addSequential(decend);
    }

}
