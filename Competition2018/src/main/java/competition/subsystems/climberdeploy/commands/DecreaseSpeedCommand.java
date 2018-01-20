package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import xbot.common.command.BaseCommand;

public class DecreaseSpeedCommand extends BaseCommand{

	ClimberdeploySubsystem deploy;

	@Inject 
	public DecreaseSpeedCommand(ClimberdeploySubsystem deploy) {
		this.deploy = deploy;
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
		deploy.decreaseSpeed();
	}

	@Override
	public void execute() {
	}
	
	@Override
	public boolean isFinished() {
		return true;
	}
}
