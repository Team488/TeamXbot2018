package competition.subsystems.offboard.commands;

import com.google.inject.Inject;

import competition.operator_interface.RumbleManager;
import xbot.common.command.BaseCommand;

public class StopRumbleCommand extends BaseCommand {
    private final RumbleManager rumbleManager;
    
    @Inject
    public StopRumbleCommand(RumbleManager rumbleManager) {
        this.rumbleManager = rumbleManager;
    }
    
    @Override
    public void initialize() {
        rumbleManager.stopDriverGamepadRumble();
    }
    
    @Override
    public void execute() {
        
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }

}
