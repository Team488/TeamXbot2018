package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;

import competition.subsystems.gripperintake.GripperIntakeSubsystem;

import xbot.common.command.BaseCommand;

public class GripperIntakeCommand extends BaseCommand {

    GripperIntakeSubsystem intake;

    @Inject
    public GripperIntakeCommand(GripperIntakeSubsystem intake) {
        this.requires(intake);
        this.intake = intake;
    }

    public void setDuration(double seconds) {
    	this.setTimeout(seconds);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        intake.intake();
    }
    
    @Override
    public boolean isFinished() {
    	return this.isTimedOut();
    }

}
