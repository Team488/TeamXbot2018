package competition.subsystems.offboard.commands;

import com.google.inject.Inject;

import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.data.TargetCubeInfo;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class IdentifyTargetCubeCommand extends xbot.common.command.BaseCommand {

    private final OffboardInterfaceSubsystem offboard;
    private final DoubleProperty manualTimeout;
    private final DoubleProperty autoTimeout;
    
    private TargetCubeInfo chosenTarget;
    
    public enum TimeoutPreset {
        Manual,
        Auto
    }
    
    @Inject
    public IdentifyTargetCubeCommand(OffboardInterfaceSubsystem offboard, XPropertyManager propMan) {
        this.offboard = offboard;

        this.manualTimeout = propMan.createPersistentProperty(getPrefix() + "Manual timeout", 0.2);
        this.autoTimeout = propMan.createPersistentProperty(getPrefix() + "Auto timeout", 1);
        
        setTimeoutPreset(TimeoutPreset.Manual);
    }
    
    public TargetCubeInfo getChosenTarget() {
        return chosenTarget;
    }
    
    public void setTimeoutPreset(TimeoutPreset timeoutPreset) {
        this.setTimeout(timeoutPreset == TimeoutPreset.Auto ? autoTimeout.get() : manualTimeout.get());
    }
    
    @Override
    public void initialize() {
        chosenTarget = null;
    }

    @Override
    public void execute() {
        if (chosenTarget == null) {
            chosenTarget = offboard.getTargetCube();
        }
    }
    
    @Override
    public boolean isFinished() {
        return this.isTimedOut() || chosenTarget != null;
    }

}
