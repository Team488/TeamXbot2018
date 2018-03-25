package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;

import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class GripperIntakeTimeCommand extends BaseCommand {

    DoubleProperty stopIntakeTime;
    GripperIntakeSubsystem intake;
    double stopTime;

    @Inject
    public void GripperIntakeCommand(GripperIntakeSubsystem intake, XPropertyManager propMan) {
        this.requires(intake);
        this.intake = intake;
        stopIntakeTime = propMan.createPersistentProperty("stopIntakeTime", 5);
    }
    @Override
    public void initialize() {
        log.info("Initializing");
        stopTime = Timer.getFPGATimestamp() + stopIntakeTime.get(); 
    }

    @Override
    public void execute() {
        if (stopTime > Timer.getFPGATimestamp()) {
        intake.intake();
        }
    }
    
    public boolean isFinished() {
        return stopTime <= Timer.getFPGATimestamp();
    }

}
