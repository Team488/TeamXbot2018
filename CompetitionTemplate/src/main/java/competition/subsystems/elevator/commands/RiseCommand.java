package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSubsystem;

@Singleton
public class RiseCommand extends BaseCommand {
	
	ElevatorSubsystem rise;
	
	boolean stop;
	
	double setTarget;
	double robotSpeed;
	
	@Inject
	public RiseCommand(ElevatorSubsystem rise) {
		this.rise = rise;
	}
	
	@Override
	public void initialize() {
		
		log.info("Initializing");
		
		rise.rise();
		
	}
	
	@Override
	public void execute() {
		
		rise.rise();
		
		if (rise.currentHeight() == setTarget) {
			if (robotSpeed <= 0.1 && robotSpeed >= -0.1 ) {
				stop = true;
			}
			
		}
	}
	
	@Override
	public boolean isFinished() {
		
		if (stop = true) {
			rise.stop();
			return true;	
		}
		else {
			return false;
		}
		
	}
	
	
	

}
