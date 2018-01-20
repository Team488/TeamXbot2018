package competition.subsystems.lean.commands;

import com.google.inject.Inject;

import competition.subsystems.lean.LeanSubsystem;
import xbot.common.command.BaseCommand;

public class LeanRightCommand extends BaseCommand {

	LeanSubsystem leaner;
	boolean stop;
	
	@Inject
	public LeanRightCommand(LeanSubsystem leaner) {
		this.leaner = leaner;
		this.requires(leaner); 
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
	}

	@Override
	public void execute() {
		leaner.leanRight();
		if (leaner.hitBar()) {
			stop = true;
		}
		else {
			stop = false;
		}
	}

	@Override
	public boolean isFinished() {
		return leaner.hitBar();
	}
	public void end() {
		leaner.stopLean();
	}

}

