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
        if (!elevator.isCalibrated()) {
            log.warn("ELEVATOR UNCALIBRATED - THIS COMMAND WILL NOT DO ANYTHING!");
        }
    }

    @Override
    public void execute() {
        if (elevator.isCalibrated()) {
            double power = pid.calculate(elevator.getTargetHeight(), elevator.getCurrentHeightInInches());
            elevator.setPower(power);
        } else {
            elevator.stop();
        }
    }
}
