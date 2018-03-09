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
    
    private double getAdjustedPower(double input) {
        if (input >= 0.1) {
            double deadband = 0.5 / 0.9;
            double jump = 0.4 / 0.9;
            double adjustedPower = (deadband * input) + jump;
            return adjustedPower;
        } else {
            return input;
        }
    }

    @Override
    public void execute() {
        double intakePower = -getAdjustedPower(oi.operatorGamepad.getLeftTrigger());
        double ejectPower = getAdjustedPower(oi.operatorGamepad.getRightTrigger());
        double totalPower = intakePower + ejectPower;
        intake.setPower(totalPower, totalPower);
    }
}
