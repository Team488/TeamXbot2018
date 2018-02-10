package competition.subsystems.climb;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimbSubsystem extends BaseSubsystem {

    final DoubleProperty ascendSpeed;
    final DoubleProperty decendSpeed;
    CommonLibFactory clf;
    public XCANTalon motor;
    ElectricalContract2018 contract;

    @Inject
    public ClimbSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        ascendSpeed = propMan.createPersistentProperty("ascendSpeed", 1);
        decendSpeed = propMan.createPersistentProperty("decendSpeed", -.1);

        if (contract.climbReady()) {
            initializeMotor();
        }
    }

    /**
     * Should only be called directly by test code. Temporary workaround to deal with the
     * "too-many-CAN-errors-crashes-robot" situation.
     */
    private void initializeMotor() {
        motor = clf.createCANTalon(contract.getClimbMaster().channel);
        motor.setInverted(contract.getClimbMaster().inverted);
    }

    /**
     * moves the winch to pull the robot up
     **/
    public void ascend() {
        motor.simpleSet(ascendSpeed.get());
    }

    /**
     * moves the winch to let the robot down
     */
    public void decend() {
        motor.simpleSet(decendSpeed.get());
    }

    /**
     * stops the winch
     */
    public void stop() {
        motor.simpleSet(0);
    }
}
