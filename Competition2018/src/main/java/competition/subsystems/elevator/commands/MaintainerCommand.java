package competition.subsystems.elevator.commands;

import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import competition.subsystems.elevator.ElevatorSubsystem;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

public class MaintainerCommand extends BaseCommand {
    
    ElevatorSubsystem elevator;
    PIDManager pid;
    double error;
    boolean isFinished;
    
    @Inject
    public MaintainerCommand(ElevatorSubsystem elevator, PIDFactory pf) {
        this.elevator = elevator;
        pid = pf.createPIDManager("Elevator", 1, 0, 0);
        pid.setErrorThreshold(0.1);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");        
    }

    @Override
    public void execute() {
        
        double power = pid.calculate(elevator.getTargetHeight(), elevator.currentHeight());
       
        elevator.setPower(power);
        
        if(elevator.currentHeight() >= elevator.getTargetHeight() -.01 && elevator.currentHeight() <= elevator.getTargetHeight() + .01) {
            isFinished = true;
        }
        else {
            isFinished = false;
        }
    }
    
    @Override
    public boolean isFinished() {
        if(isFinished) {
            return true;
        }
        else {
            return false;
        }
    }

}
