package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;

@Singleton
public class MoveToMaxHeightCommand extends BaseCommand {

    ElevatorSubsystem elevator;
    PIDManager pid;

    @Inject
    public MoveToMaxHeightCommand(ElevatorSubsystem elevator, PIDFactory pf) {
        this.elevator = elevator;
        pid = pf.createPIDManager("Elevator", 0.1, 0, 0);
        pid.setErrorThreshold(0.1);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        if (!elevator.isCalibrated()) {
            log.warn("ELEVATOR UNCALIBRATED - THIS COMMAND WILL NOT DO ANYTHING!");
        }
    }

    @Override
    public void execute() {
        if (elevator.isCalibrated()) {
            double power = pid.calculate(elevator.getMaxHeight(), elevator.getCurrentHeight());
            elevator.setPower(power);
        }
    }

    @Override
    public boolean isFinished() {
        return pid.isOnTarget();
    }
}
