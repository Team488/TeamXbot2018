package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import xbot.common.command.BaseCommand;

public class RetractClimberArmCommand extends BaseCommand {

	ClimberdeploySubsystem deploy;
	boolean stop;

	@Inject
	public RetractClimberArmCommand (ClimberdeploySubsystem deploy) {
		this.deploy = deploy;
		this.requires(deploy);
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");		
	}

	@Override
	public void execute() {
		deploy.retractClimberArm();
		if (deploy.isRetracted()) {
			stop = true;
		}
	}

	@Override
	public boolean isFinished() {
		return deploy.hitBarHeight();
	}
	public void end() {
		deploy.stopClimberArm();
	}
}
