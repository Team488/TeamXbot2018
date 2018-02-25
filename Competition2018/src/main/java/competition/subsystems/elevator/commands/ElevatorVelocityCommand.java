package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;

public class ElevatorVelocityCommand extends BaseCommand {

    ElevatorSubsystem elevator;
    OperatorInterface oi;
    double throttle;
    
    @Inject
    public ElevatorVelocityCommand(ElevatorSubsystem elevator, OperatorInterface oi) {
        this.elevator = elevator;
        this.oi = oi;
        this.requires(elevator);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        elevator.getVelocityPid().reset();
    }

    @Override
    public void execute() {
        
        double positionOutput = elevator.getPositionalPid().calculate(elevator.getTargetHeight(), elevator.getCurrentHeightInInches());
        double powerDelta = elevator.getVelocityPid().calculate(positionOutput*16, elevator.getVelocityInchesPerSecond());
        throttle += powerDelta;
        throttle = MathUtils.constrainDouble(throttle, -0.2, 1);
        elevator.setPower(throttle);
    }

}
