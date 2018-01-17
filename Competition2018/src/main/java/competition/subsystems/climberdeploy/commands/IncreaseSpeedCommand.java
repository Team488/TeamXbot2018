package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import xbot.common.command.BaseCommand;

public class IncreaseSpeedCommand extends BaseCommand {
	
	ClimberdeploySubsystem deploy;

	@Inject
	public IncreaseSpeedCommand(ClimberdeploySubsystem deploy) {
		this.deploy = deploy;
	}
	@Override
	public void initialize() {
		log.info("Initializing");
		deploy.increaseSpeed();
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
