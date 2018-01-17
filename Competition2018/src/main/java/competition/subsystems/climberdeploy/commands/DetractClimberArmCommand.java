package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import xbot.common.command.BaseCommand;

public class DetractClimberArmCommand extends BaseCommand {

	ClimberdeploySubsystem deploy;
	boolean stop;

	@Inject
	public DetractClimberArmCommand (ClimberdeploySubsystem deploy) {
		this.deploy = deploy;
		this.requires(deploy);
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");		
	}

	@Override
	public void execute() {
		deploy.detractClimberArm();
		if (deploy.isRetracted()) {
			stop = true;
		}
		else {
			stop = false;
		}
	}

	@Override
	public boolean isFinished() {
		if (stop) {
			return true;
		}
		else {
			return false;
		}
	}
}
