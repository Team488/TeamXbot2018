package competition.subsystems.offboard.commands;

import com.google.inject.Inject;

import xbot.common.command.BaseCommandGroup;

public class IdentifyAndPurePursuitToVisibleCubeCommand extends BaseCommandGroup {
    private final IdentifyTargetCubeCommand identifyCommand;
    
    @Inject
    public IdentifyAndPurePursuitToVisibleCubeCommand(IdentifyTargetCubeCommand identify, PurePursuitToVisibleCubeCommand pursue) {
        this.identifyCommand = identify;
        pursue.setTargetCubeSupplier(() -> identify.getChosenTarget());
        
        this.addSequential(identify);
        this.addSequential(pursue);
    }
    
    public void setTimeoutPreset(IdentifyTargetCubeCommand.TimeoutPreset timeoutPreset) {
        identifyCommand.setTimeoutPreset(timeoutPreset);
    }
}
