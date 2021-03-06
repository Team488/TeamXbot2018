package competition.subsystems.climb.commands;

import com.google.inject.Inject;
import competition.subsystems.climb.ClimbSubsystem;
import xbot.common.command.BaseCommand;

public class StopClimberCommand extends BaseCommand {

    ClimbSubsystem climb;

    @Inject
    public StopClimberCommand(ClimbSubsystem climb) {
        this.climb = climb;
        this.requires(climb);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        climb.stop();
    }

}
