package competition.subsystems.offboard.commands;

import com.google.inject.Inject;

import xbot.common.command.BaseCommandGroup;

public class IdentifyAndPurePursuitToVisibleCubeCommand extends BaseCommandGroup {
    private final IdentifyTargetCubeCommand identifyCommand;
    
    @Inject
    public IdentifyAndPurePursuitToVisibleCubeCommand(
            StopRumbleCommand stopRumble,
            IdentifyTargetCubeCommand identify,
            RumbleIfNoTargetAvailableCommand rumble,
            PurePursuitToVisibleCubeCommand pursue) {
        this.identifyCommand = identify;
        pursue.setTargetCubeSupplier(() -> identify.getChosenTarget());
        
        // TODO: Don't rumble in auto
        this.addSequential(stopRumble);
        this.addSequential(identify);
        this.addSequential(rumble);
        this.addSequential(pursue);
    }
    
    public void setTimeoutPreset(IdentifyTargetCubeCommand.TimeoutPreset timeoutPreset) {
        identifyCommand.setTimeoutPreset(timeoutPreset);
    }
}
