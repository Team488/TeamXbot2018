package competition.subsystems.elevator.commands;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import competition.subsystems.elevator.ElevatorSubsystem;
import com.google.inject.Inject;
import xbot.common.math.PIDManager;

public class ElevatorMaintainerCommand extends BaseCommand {
    
    ElevatorSubsystem elevator;
    PIDManager pid;
    
    @Inject
    public ElevatorMaintainerCommand(ElevatorSubsystem elevator, PIDFactory pf) {
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
        double power = pid.calculate(elevator.getTargetHeight(), elevator.getCurrentHeight());
        elevator.setPower(power);
    }
    
}
