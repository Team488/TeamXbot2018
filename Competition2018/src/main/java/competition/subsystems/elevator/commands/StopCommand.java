package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSubsystem;


@Singleton
public class StopCommand extends BaseSubsystem {
	
	double power;
	
	public void execute() {
		
		power = 0;
		
	}
}
