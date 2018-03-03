package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.DecendClimberCommand;
import competition.subsystems.climb.commands.ReleasePawlCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DisengageWinchAndReleasePawlCommandGroup extends CommandGroup{
    
    @Inject
    public DisengageWinchAndReleasePawlCommandGroup(
            AscendClimberCommand ascend,
            ReleasePawlCommand release,
            DecendClimberCommand decend) 
    {
        this.addParallel(release);
        this.addSequential(ascend, 0.1);
        this.addSequential(decend);
    }

}