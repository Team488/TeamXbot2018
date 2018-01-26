package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSubsystem;


@Singleton
public class LowerCommand extends BaseCommand {
	
	ElevatorSubsystem lower;
	
	@Inject
	public LowerCommand(ElevatorSubsystem lower) {
		this.lower = lower;
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void execute() {
		lower.lower();
		
	}

}
