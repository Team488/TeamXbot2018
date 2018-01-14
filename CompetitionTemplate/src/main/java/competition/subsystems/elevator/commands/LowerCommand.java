package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSubsystem;


@Singleton
public class LowerCommand extends BaseCommand {
	
	ElevatorSubsystem lower;
	
	boolean stop;
	
	double setTarget;
	double speed;
	
	
	@Inject
	public LowerCommand(ElevatorSubsystem lower) {
		this.lower = lower;
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
		
		lower.lower();
	}
	
	@Override
	public void execute() {
		
		lower.lower();
		
		if (lower.currentHeight() == setTarget) {
			if (speed <= 0.1 && speed >= -0.1) {
				stop = true;
			}
		}
		
	}
	
	@Override 
	public boolean isFinished() {
		
		if (stop = true) {
			lower.stop();
			return true;
		}
		else {
			return false;
		}
	}

}
