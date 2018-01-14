package competition.subsystems.lean.commands;

import com.google.inject.Inject;

import competition.subsystems.lean.LeanSubsystem;
import xbot.common.command.BaseCommand;

public class StopLeaningCommand extends BaseCommand {

	LeanSubsystem leaner;
	
	@Inject
	public StopLeaningCommand(LeanSubsystem leaner) {
		this.leaner = leaner;
		this.requires(leaner); 
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
	}

	@Override
	public void execute() {
		leaner.stopLean();
	}

}
