package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.commands.ExtendClimberArmCommand;
import xbot.common.command.BaseCommandGroup;

public class PrepareToClimbCommandGroup extends BaseCommandGroup {

    @Inject
    public PrepareToClimbCommandGroup(DisengageWinchAndReleasePawlCommandGroup payOutWinch,
            ExtendClimberArmCommand extendArm) {
        this.addParallel(payOutWinch);
        this.addParallel(extendArm);
    }
}
