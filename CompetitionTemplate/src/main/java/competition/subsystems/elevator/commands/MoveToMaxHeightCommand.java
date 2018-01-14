package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSubsystem;

@Singleton
public class MoveToMaxHeight extends BaseCommand {
	
	ElevatorSubsystem moveMax;
	
	boolean stop;
	
	double max;
	double speed;
	
	double leftConstant;
	double rightConstant;
	double oldError;

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		log.info("Initializing");
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		double error = max - moveMax.currentHeight();
		double changeInError = error - oldError;
		
		double power = leftConstant * error - rightConstant * changeInError;
		
		//give power to thingy, to move cube up to max
		
		oldError = error;
		
		if ( moveMax.currentHeight() >= max-0.1) {
			if ( speed >= -0.1 && speed <= 0.1) {
				stop = true;
			}
		}
	}
	
	@Override
	public boolean isFinished() {
		
		if (stop = true) {
			return true;
		}
		
		return false;
	}
	

}
