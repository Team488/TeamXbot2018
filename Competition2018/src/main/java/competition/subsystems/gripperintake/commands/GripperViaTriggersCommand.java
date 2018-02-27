package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import xbot.common.command.BaseCommand;

public class GripperViaTriggersCommand extends BaseCommand {

    GripperIntakeSubsystem intake;
    OperatorInterface oi;
    
    @Inject
    public GripperViaTriggersCommand(GripperIntakeSubsystem intake, OperatorInterface oi) {
        this.intake = intake;
        this.oi = oi;
        this.requires(intake);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double intakePower = oi.operatorGamepad.getLeftTrigger();
        double ejectPower = -oi.operatorGamepad.getRightTrigger();
        double totalPower = intakePower + ejectPower;
        intake.setPower(totalPower, totalPower);
    }
}
