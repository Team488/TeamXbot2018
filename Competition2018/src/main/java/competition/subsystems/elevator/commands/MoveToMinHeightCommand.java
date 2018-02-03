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

    double min;

    @Inject
    public MoveToMinHeightCommand(ElevatorSubsystem elevator, PIDFactory pf) {
        this.elevator = elevator;
        pid = pf.createPIDManager("Elevator", 0.1, 0, 0);
        pid.setErrorThreshold(0.1);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        if (!elevator.isCalibrated()) {
            log.warn("THE ELEVATOR WILL NOT BE ABLE TO RUN UNDER AUTOMATIC CONTROL!");
        }
    }

    @Override
    public void execute() {
        min = elevator.getMinHeight();
        if (elevator.isCalibrated()) {
            double power = pid.calculate(min, elevator.getCurrentHeight());
            elevator.setPower(power);
        }

    }

    @Override
    public boolean isFinished() {
        return pid.isOnTarget();
    }

}
