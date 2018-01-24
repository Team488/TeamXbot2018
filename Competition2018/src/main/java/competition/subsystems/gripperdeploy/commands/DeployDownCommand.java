package competition.subsystems.gripperdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import xbot.common.command.BaseCommand;

public class DeployDownCommand extends BaseCommand {
	
	GripperDeploySubsystem gripperDeploy;

	@Inject
	public DeployDownCommand(GripperDeploySubsystem gripperDeploy) {
		this.gripperDeploy = gripperDeploy;
		this.requires(gripperDeploy);
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
	}

	@Override
	public void execute() {
		gripperDeploy.deployDown();
	}
	
	@Override
	public boolean isFinished() {
		return true;
	}
	
	public void end() {
		gripperDeploy.stopGripper();
	}
}
