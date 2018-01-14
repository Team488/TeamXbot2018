package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSubsystem;

@Singleton
public class MoveToMinHeight extends BaseCommand {
	
	ElevatorSubsystem moveMin;
	
	boolean stop;
	
	double min;
	double speed;
	double oldError;
	
	double leftConstant;
	double rightConstant;

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		log.info("Initializing");
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		double error = min - moveMin.currentHeight();
		double changeInError = error - oldError;
		
		double power = leftConstant * error - rightConstant * changeInError;
		
		// give power to elevator thingy to bring cube down to in
		
		oldError = error;
		
		if (moveMin.currentHeight() <= min + 0.1) {
			if (speed >= -0.1 && speed <= 0.1) {
				stop = true;
			}
		}
	}
	
	@Override
	public boolean isFinished() {
		if (stop = true ) {
			return true;
		}
		
		return false;
	}

}
