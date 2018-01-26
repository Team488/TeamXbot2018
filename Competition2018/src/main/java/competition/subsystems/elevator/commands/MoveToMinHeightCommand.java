package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

@Singleton
public class MoveToMinHeightCommand extends BaseCommand {
	
	ElevatorSubsystem elevator;
	PIDManager pid;
	
	boolean stop;
	
	double min = 0;
	
	@Inject
	public MoveToMinHeightCommand(ElevatorSubsystem elevator, PIDFactory pf) { 
		this.elevator = elevator;
		pid = pf.createPIDManager("Elevator", 0.1, 0, 0);
		pid.setErrorThreshold(0.1);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		log.info("Initializing");
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		double power = pid.calculate(min, elevator.currentHeight());
		
		elevator.setPower(power);
		
	}
	
	@Override
	public boolean isFinished() {
		return pid.isOnTarget();
	}

}
