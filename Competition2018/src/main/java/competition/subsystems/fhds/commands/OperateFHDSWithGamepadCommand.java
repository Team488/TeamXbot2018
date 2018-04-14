package competition.subsystems.fhds.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.fhds.FlyingHookDeliverySensorSubsystem;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class OperateFHDSWithGamepadCommand extends BaseCommand {

    private final FlyingHookDeliverySensorSubsystem fhdsSubsystem;
    private final OperatorInterface oi;

    @Inject
    public OperateFHDSWithGamepadCommand(FlyingHookDeliverySensorSubsystem fhdsSubsystem, WristSubsystem wrist,
            ElevatorSubsystem elevator, OperatorInterface oi) {
        this.fhdsSubsystem = fhdsSubsystem;
        this.oi = oi;

        this.requires(fhdsSubsystem);

        // Require other systems that might be using the operator gamepad
        this.requires(wrist);
        this.requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        fhdsSubsystem.handleOperatorInput(oi.operatorGamepad.getLeftVector(), oi.operatorGamepad.getRightVector());
    }
}
