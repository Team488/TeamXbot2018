package competition.subsystems.lean.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.lean.LeanSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class LeanWithDpadCommand extends BaseCommand {

    final LeanSubsystem leanSubsystem;
    final OperatorInterface oi;
    final DoubleProperty leanPowerProp;

    @Inject
    public LeanWithDpadCommand(OperatorInterface oi, LeanSubsystem leanSubsystem, XPropertyManager propMan) {
        this.oi = oi;
        this.leanSubsystem = leanSubsystem;
        this.requires(this.leanSubsystem);
        leanPowerProp = propMan.createPersistentProperty(getPrefix() + "Power", 0.25);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double power = 0;
        if (oi.driverGamepad.getPOV() == 90) {
            power = leanPowerProp.get();
        }
        if (oi.driverGamepad.getPOV() == 270) {
            power = -leanPowerProp.get();
        }
        
        leanSubsystem.setPower(power);
    }
}
