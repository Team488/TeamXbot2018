package competition.subsystems.gripperdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import xbot.common.command.BaseCommand;

public class GripperDeployUpCommand extends BaseCommand {
	
	GripperDeploySubsystem gripperDeploy;

	@Inject
	public GripperDeployUpCommand(GripperDeploySubsystem gripperDeploy) {
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
	
	public void end() {
		gripperDeploy.stopGripper();
	}
}
