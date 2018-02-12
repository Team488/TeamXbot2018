package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;

import competition.subsystems.gripperintake.GripperIntakeSubsystem;

import xbot.common.command.BaseCommand;

public class GripperStopCommand extends BaseCommand {

    GripperIntakeSubsystem intake;
    
    @Inject
    public GripperStopCommand(GripperIntakeSubsystem intake) {
        this.requires(intake);
        this.intake = intake;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        intake.stop();
    }

}
