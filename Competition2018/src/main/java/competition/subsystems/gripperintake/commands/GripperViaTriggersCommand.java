package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.math.MathUtils;

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
        double intakePower;
        double ejectPower;
        double slope = 0.5 / 0.9;
        double yInt = 0.4 / 0.9;
        if (oi.operatorGamepad.getLeftTrigger() >= 0.1) {
            intakePower = (slope * -oi.operatorGamepad.getLeftTrigger()) - yInt;
        }
        else {
            intakePower = -oi.operatorGamepad.getLeftTrigger();
        }
        
        if (oi.operatorGamepad.getRightTrigger() >= 0.1) {
            ejectPower = (slope * oi.operatorGamepad.getRightTrigger()) + yInt;
        }
        else {
            ejectPower = oi.operatorGamepad.getRightTrigger();
        }
        double totalPower = intakePower + ejectPower;
        intake.setPower(totalPower, totalPower);
    }
}
