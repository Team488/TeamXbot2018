package competition.subsystems.gripperdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import xbot.common.command.BaseCommand;

public class DeployUpCommand extends BaseCommand {
	
	GripperDeploySubsystem gripperDeploy;

	@Inject
	public DeployUpCommand(GripperDeploySubsystem gripperDeploy) {
		this.gripperDeploy = gripperDeploy;
		this.requires(gripperDeploy);
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
	}

	@Override
	public void execute() {
		gripperDeploy.deployUp();
	}
	
	@Override
	public boolean isFinished() {
		return true;
	}
	
	public void end() {
		gripperDeploy.stopGripper();
	}
}
