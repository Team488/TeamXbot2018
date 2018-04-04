package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class GripperDropCubeCommand extends BaseCommand{

    GripperIntakeSubsystem intake;
    OperatorInterface oi;

    final DoubleProperty dropPower;
    
    @Inject 
    public GripperDropCubeCommand(GripperIntakeSubsystem intake, OperatorInterface oi, XPropertyManager propMan) {
        this.intake = intake;
        this.oi = oi;
        this.requires(intake);
        
        dropPower = propMan.createPersistentProperty(getPrefix() + "Cube Drop Power", .1);
    }
    @Override
    public void initialize() {
        log.info("Initializing");        
    }

    @Override
    public void execute() {
        intake.setPower(dropPower.get(), dropPower.get());
    }

}
