package competition.subsystems.offboard.commands;

import java.util.function.Supplier;

import com.google.inject.Inject;

import competition.operator_interface.RumbleManager;
import xbot.common.command.BaseCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class RumbleIfNoTargetAvailableCommand extends BaseCommand {
    
    private Supplier<Boolean> isAvailableSupplier;
    private final DoubleProperty rumbleTime;
    private final DoubleProperty rumbleIntensity;
    private final RumbleManager rumbleManager;
    
    @Inject
    public RumbleIfNoTargetAvailableCommand(XPropertyManager propMan, RumbleManager rumbleManager) {
        this.rumbleManager = rumbleManager;
        rumbleTime = propMan.createPersistentProperty(getPrefix() + "Rumble time", 0.5);
        rumbleIntensity = propMan.createPersistentProperty(getPrefix() + "Rumble intensity", 1.0);
    }
    
    public void setIsAvailableSupplier(Supplier<Boolean> isAvailableSupplier) {
        this.isAvailableSupplier = isAvailableSupplier;
    }
    
    @Override
    public void initialize() {
        if (isAvailableSupplier != null) {
            Boolean isAvailable = isAvailableSupplier.get();
            if (isAvailable != true) {
                rumbleManager.rumbleDriverGamepad(rumbleIntensity.get(), rumbleTime.get());
            }
        }
    }
    
    @Override
    public void execute() {
        // Intentionally left blank
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
