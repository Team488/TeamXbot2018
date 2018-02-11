package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristStopCommand extends BaseCommand {

    WristSubsystem gripperDeploy;

    @Inject
    public WristStopCommand(WristSubsystem gripperDeploy) {
        this.gripperDeploy = gripperDeploy;
        this.requires(gripperDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        gripperDeploy.stop();
    }
}
