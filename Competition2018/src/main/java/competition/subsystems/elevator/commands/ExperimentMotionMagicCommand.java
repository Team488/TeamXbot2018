package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class ExperimentMotionMagicCommand extends BaseCommand {

    final ElevatorSubsystem elevator;
    final DoubleProperty goalHeight;
    
    @Inject
    public ExperimentMotionMagicCommand(ElevatorSubsystem elevator, XPropertyManager propMan) {
        this.elevator = elevator;
        this.requires(elevator);
        goalHeight = propMan.createPersistentProperty(getPrefix() + "Experimental MM Goal Height", 3);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        if (elevator.isCalibrated()) {
            elevator.motionMagicToHeight(goalHeight.get());
        }
        else {
            elevator.stop();
        }
    }

}
