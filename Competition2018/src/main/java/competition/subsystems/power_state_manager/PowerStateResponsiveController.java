package competition.subsystems.power_state_manager;

public interface PowerStateResponsiveController {
    void onEnterLowBatteryMode();
    void onLeaveLowBatteryMode();
}
