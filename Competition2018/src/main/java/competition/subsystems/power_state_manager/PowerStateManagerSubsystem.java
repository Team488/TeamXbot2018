package competition.subsystems.power_state_manager;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class PowerStateManagerSubsystem extends BaseSubsystem implements PeriodicDataSource {

    private final BooleanProperty isLowBatteryModeProp;
    private final Latch isLowBatteryModeLatch;
    
    private final ArrayList<PowerStateResponsiveController> responsiveControllers;
    
    @Inject
    public PowerStateManagerSubsystem(XPropertyManager propMan) {
        isLowBatteryModeProp = propMan.createEphemeralProperty(getPrefix() + "Is low battery mode?", false);
        isLowBatteryModeLatch = new Latch(false, EdgeType.Both, this::onChangeLowBatteryMode);
        
        responsiveControllers = new ArrayList<>();
    }
    
    public void registerResponsiveController(PowerStateResponsiveController controller) {
        this.responsiveControllers.add(controller);
    }
    
    public boolean isLowBatteryMode() {
        return isLowBatteryModeProp.get();
    }
    
    public void setLowBatteryMode(boolean isLowBattery) {
        this.isLowBatteryModeProp.set(isLowBattery);
        isLowBatteryModeLatch.setValue(isLowBattery);
    }
    
    public void enterLowBatteryMode() {
        setLowBatteryMode(true);
    }
    
    public void leaveLowBatteryMode() {
        setLowBatteryMode(false);
    }
    
    private void onChangeLowBatteryMode(EdgeType edge) {
        if (edge == EdgeType.RisingEdge) {
            this.onEnterLowBatteryMode();
        }
        else if (edge == EdgeType.FallingEdge) {
            this.onLeaveLowBatteryMode();
        }
    }

    private void onEnterLowBatteryMode() {
        log.warn("Entering low battery mode!");
        this.responsiveControllers.forEach(controller -> controller.onEnterLowBatteryMode());
    }
    
    private void onLeaveLowBatteryMode() {
        log.warn("Leaving low battery mode!");
        this.responsiveControllers.forEach(controller -> controller.onLeaveLowBatteryMode());
    }
    
    @Override
    public void updatePeriodicData() {
        isLowBatteryModeLatch.setValue(isLowBatteryModeProp.get());
    }
}
