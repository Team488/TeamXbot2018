package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.command.BaseSubsystem;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

@Singleton
public class MoveToMaxHeightCommand extends BaseCommand {
	
	ElevatorSubsystem elevator;
	PIDManager pid;
	
	boolean stop;
	
	double max = 90;
	double speed;
	
	double leftConstant;
	double rightConstant;
	double oldError;
	
	@Inject
	public MoveToMaxHeightCommand(ElevatorSubsystem elevator, PIDFactory pf) {
		// TODO Auto-generated constructor stub
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
		
		double power = pid.calculate(max, elevator.currentHeight());
		
		elevator.setPower(power);
	}
	
	@Override
	public boolean isFinished() {
		return pid.isOnTarget();
	}
	

}
