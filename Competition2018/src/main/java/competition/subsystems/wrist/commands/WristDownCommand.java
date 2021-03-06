package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristDownCommand extends BaseCommand {

    WristSubsystem wrist;

    @Inject
    public WristDownCommand(WristSubsystem wrist) {
        this.wrist = wrist;
        this.requires(wrist);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        wrist.goDown();
    }

    public void end() {
        wrist.stop();
    }
}
