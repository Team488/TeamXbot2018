package competition.subsystems.elevator.commands;
import xbot.common.command.BaseSetpointCommand;
import com.google.inject.Inject;
import competition.subsystems.elevator.ElevatorSubsystem;

public class SetElevatorTargetHeightCommand extends BaseSetpointCommand{

    double height;
    ElevatorSubsystem elevator;
    
    @Inject
    public SetElevatorTargetHeightCommand(ElevatorSubsystem elevator) {
        super(elevator);
        this.elevator = elevator;
    }

    public void setGoalHeight(double height) {
        this.height = height;
    }
  
    @Override
    public void initialize() {
        log.info("Initializing");
        elevator.setTargetHeight(height);
    }

    @Override
    public void execute() {
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
 
}
