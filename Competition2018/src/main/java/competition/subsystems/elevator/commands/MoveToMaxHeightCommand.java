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

    double max = 90;

    @Inject
    public MoveToMaxHeightCommand(ElevatorSubsystem elevator, PIDFactory pf) {
        this.elevator = elevator;
        pid = pf.createPIDManager("Elevator", 0.1, 0, 0);
        pid.setErrorThreshold(0.1);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double power = pid.calculate(max, elevator.currentHeight());

        elevator.setPower(power);
    }

    @Override
    public boolean isFinished() {
        return pid.isOnTarget();
    }

}
