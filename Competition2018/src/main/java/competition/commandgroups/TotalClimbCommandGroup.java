package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.commands.RetractClimberArmCommand;
import xbot.common.command.BaseCommandGroup;

public class TotalClimbCommandGroup extends BaseCommandGroup {

    @Inject
    public TotalClimbCommandGroup(
            EngageWinchAndLockPawlCommandGroup winchUp, 
            RetractClimberArmCommand retractPhaseOne,
            RetractClimberArmCommand retractPhaseTwo) {
        this.addSequential(retractPhaseOne, 4);
        this.addParallel(winchUp);
        this.addParallel(retractPhaseTwo);
    }
}
