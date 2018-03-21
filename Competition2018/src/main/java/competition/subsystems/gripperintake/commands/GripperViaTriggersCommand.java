package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class GripperViaTriggersCommand extends BaseCommand {

    GripperIntakeSubsystem intake;
    OperatorInterface oi;
    
    final DoubleProperty linearDeadbandProp;
    final DoubleProperty powerJumpProp;
    
    @Inject
    public GripperViaTriggersCommand(GripperIntakeSubsystem intake, OperatorInterface oi, XPropertyManager propMan) {
        this.intake = intake;
        this.oi = oi;
        this.requires(intake);
        
        linearDeadbandProp = propMan.createPersistentProperty(getPrefix() + "Linear Deadband", 0.1);
        powerJumpProp = propMan.createPersistentProperty(getPrefix() + "Power Jump", 0.4);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    public double getAdjustedPower(double input) {
        double range = 1 - linearDeadbandProp.get();
        double change = 1 - powerJumpProp.get();
        double slope = 1;
        if (range != 0) {
            slope = change/range;
        }
        
        if (input >= linearDeadbandProp.get()) {
            return (input-linearDeadbandProp.get()) * slope + powerJumpProp.get();
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
