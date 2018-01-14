package competition.subsystems.lean.commands;

import com.google.inject.Inject;

import competition.subsystems.lean.LeanSubsystem;
import xbot.common.command.BaseCommand;

public class IncreaseSpeedCommand extends BaseCommand {

	LeanSubsystem leaner;

	@Inject
	public IncreaseSpeedCommand(LeanSubsystem leaner) {
		this.leaner = leaner;
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
	}

	@Override
	public void execute() {
	}

}


