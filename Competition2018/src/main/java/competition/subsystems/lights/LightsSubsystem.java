package competition.subsystems.lights;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XRelay;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class LightsSubsystem extends BaseSubsystem {

    final XRelay light;
    
    @Inject
    public LightsSubsystem(CommonLibFactory clf, ElectricalContract2018 contract) {
        light = clf.createRelay(contract.getAutonomousLight().channel);
        light.setInverted(contract.getAutonomousLight().inverted);
    }
    
    public void setOn(boolean value) {
        if (value) {
            light.setForward();
        } else {
            light.stop();
        }   
    }
}
