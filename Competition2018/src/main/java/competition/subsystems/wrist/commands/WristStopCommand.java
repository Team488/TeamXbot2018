package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristStopCommand extends BaseCommand {

    WristSubsystem wrist;

    @Inject
    public WristStopCommand(WristSubsystem wrist) {
        this.wrist = wrist;
        this.requires(wrist);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        wrist.stop();
    }
}
