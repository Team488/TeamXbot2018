package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.gripperintake.GripperIntakeSubsystem;

import xbot.common.command.BaseCommand;

@Singleton
public class GripperRightDominantCommand extends BaseCommand {

    GripperIntakeSubsystem intake;

    @Inject
    public GripperRightDominantCommand(GripperIntakeSubsystem intake) {
        this.requires(intake);
        this.intake = intake;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        intake.intakerightDominant();
    }

}
