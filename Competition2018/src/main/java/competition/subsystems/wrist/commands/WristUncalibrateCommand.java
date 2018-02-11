package competition.subsystems.wrist.commands;

import com.google.inject.Inject;

import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class WristUncalibrateCommand extends BaseCommand {

    WristSubsystem wrist;
    
    @Inject
    public WristUncalibrateCommand(WristSubsystem wrist) {
        this.wrist = wrist;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        wrist.uncalibrate();
    }

    @Override
    public void execute() {
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
