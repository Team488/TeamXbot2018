package competition.subsystems.power_state_manager.commands;

import com.google.inject.Inject;

import competition.subsystems.power_state_manager.PowerStateManagerSubsystem;
import xbot.common.command.BaseCommand;

public class EnterLowBatteryModeCommand extends BaseCommand {

    PowerStateManagerSubsystem powerSubsystem;

    @Inject
    public EnterLowBatteryModeCommand(PowerStateManagerSubsystem powerSubsystem) {
        this.powerSubsystem = powerSubsystem;
        this.requires(powerSubsystem);
    }

    @Override
    public void initialize() {
        powerSubsystem.enterLowBatteryMode();
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
