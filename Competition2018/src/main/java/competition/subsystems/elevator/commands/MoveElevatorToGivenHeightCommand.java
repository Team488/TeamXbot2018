package competition.subsystems.elevator.commands;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class MoveElevatorToGivenHeightCommand extends BaseCommand {
	ElevatorSubsystem elevator;
	DoubleProperty errorThreshold;
	
	public MoveElevatorToGivenHeightCommand(ElevatorSubsystem subsystem, XPropertyManager propMan) {
		this.elevator = subsystem;
		this.requires(subsystem);
		errorThreshold = propMan.createPersistentProperty("Elevator delta height threshold", 0.1);
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void execute() {
		double error = elevator.currentHeight() - elevator.getTargetHeight();
		if(error > 0) {
			elevator.lower();
		} else if(error < 0) {
			elevator.rise();
		}
	}
	
	@Override
	public boolean isFinished() { // make sure stable for a certain time
		return Math.abs(elevator.currentHeight() - elevator.getTargetHeight()) <= errorThreshold.get();
	}

	@Override
	public void end() {
		
	}
}
