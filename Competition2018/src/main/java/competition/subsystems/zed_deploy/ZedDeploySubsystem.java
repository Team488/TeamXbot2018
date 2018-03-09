package competition.subsystems.zed_deploy;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XServo;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ZedDeploySubsystem extends BaseSubsystem {

    private final DoubleProperty extendedPositionProp;
    private final DoubleProperty retractedPositionProp;
    private final BooleanProperty forceDisabledProp;
    private final XServo servo;
    private boolean isExtended = false;

    @Inject
    public ZedDeploySubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        servo = clf.createServo(contract.getZedDeploy().channel);

        extendedPositionProp = propMan.createPersistentProperty(getPrefix()+"Extended position", 0);
        retractedPositionProp = propMan.createPersistentProperty(getPrefix()+"Retracted position", 90);
        forceDisabledProp = propMan.createPersistentProperty(getPrefix()+"Force disable", false);
    }

    public void setIsExtended(boolean isExtended) {
        if (!forceDisabledProp.get()) {
            this.isExtended = isExtended;
            servo.set(isExtended ? extendedPositionProp.get() : retractedPositionProp.get());
        }
    }
    
    public boolean getIsExtended() {
        return this.isExtended;
    }
}
