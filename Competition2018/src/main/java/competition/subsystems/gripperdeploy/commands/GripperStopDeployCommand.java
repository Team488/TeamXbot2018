package competition.subsystems.gripperdeploy.commands;

import com.google.inject.Inject;

import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import xbot.common.command.BaseCommand;

public class StopDeployCommand extends BaseCommand{

	GripperDeploySubsystem gripperDeploy;
	
	@Inject
	public StopDeployCommand(GripperDeploySubsystem gripperDeploy) {
		this.gripperDeploy = gripperDeploy;
		this.requires(gripperDeploy);
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
	}

	@Override
	public void execute() {
		gripperDeploy.stopGripper();
	}
}
