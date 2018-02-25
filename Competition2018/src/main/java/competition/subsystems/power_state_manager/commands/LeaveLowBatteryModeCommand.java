package competition.subsystems.power_state_manager.commands;

import com.google.inject.Inject;

import competition.subsystems.power_state_manager.PowerStateManagerSubsystem;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class LeaveLowBatteryModeCommand extends BaseCommand {

    PowerStateManagerSubsystem powerSubsystem;

    @Inject
    public LeaveLowBatteryModeCommand(PowerStateManagerSubsystem powerSubsystem) {
        this.powerSubsystem = powerSubsystem;
        this.requires(powerSubsystem);
    }

    @Override
    public void initialize() {
        powerSubsystem.leaveLowBatteryMode();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void execute() {
        // Intentionally left blank
    }
}
