package competition.subsystems.climberdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.climberdeploy.ClimberdeploySubsystem;
import xbot.common.command.BaseCommand;

public class ExtendClimberArmCommand extends BaseCommand {
	
	ClimberdeploySubsystem deploy;
	boolean stop = false;
	
	@Inject
	public ExtendClimberArmCommand (ClimberdeploySubsystem deploy) {
		this.deploy = deploy;
		this.requires(deploy);
	}

	@Override
	public void initialize() {
		log.info("Initializing");
	}

	@Override
	public void execute() {
		deploy.extendClimberArm();
		if (deploy.hitBarHeight()) {
			stop = true;
			deploy.stopClimberArm();
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
