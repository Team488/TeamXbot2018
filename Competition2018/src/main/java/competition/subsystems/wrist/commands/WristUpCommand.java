package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristUpCommand extends BaseCommand {

    WristSubsystem gripperDeploy;

    @Inject
    public WristUpCommand(WristSubsystem gripperDeploy) {
        this.gripperDeploy = gripperDeploy;
        this.requires(gripperDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        gripperDeploy.deployUp();
    }

    public void end() {
        gripperDeploy.stopGripper();
    }
}
