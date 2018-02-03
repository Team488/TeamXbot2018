package competition.subsystems.elevator.commands;

import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import competition.subsystems.elevator.ElevatorSubsystem;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import xbot.common.math.PIDFactory;
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
        if (!elevator.isCalibrated()) {
            log.info("Elevator uncalibrated");
        }
    }

    @Override
    public void execute() {
        if (elevator.isCalibrated()) {
            double power = pid.calculate(elevator.getTargetHeight(), elevator.currentHeight());
            elevator.setPower(power);
        }
    }
}
