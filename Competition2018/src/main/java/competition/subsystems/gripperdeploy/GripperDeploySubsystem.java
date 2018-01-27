package competition.subsystems.gripperdeploy;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class GripperDeploySubsystem extends BaseSubsystem {
    final DoubleProperty gripperDeploySpeed;
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;

    public XCANTalon motor;

    @Inject
    GripperDeploySubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        gripperDeploySpeed = propMan.createPersistentProperty("gripperDeploySpeed", .5);
        
        if (contract.wristReady()) {
            temporaryHack();
        }
    }

    public void temporaryHack() {
        motor = clf.createCANTalon(contract.getWristMaster().channel);
        motor.setInverted(contract.getWristMaster().inverted);
    }

    /**
     * angles the Gripper up
     */
    public void deployUp() {
        motor.simpleSet(gripperDeploySpeed.get());
    }

    /**
     * angles the Gripper down
     */
    public void deployDown() {
        motor.simpleSet(-gripperDeploySpeed.get());
    }

    /**
     * stops the Gripper
     */
    public void stopGripper() {
        motor.simpleSet(0);
    }

    /**
     * returns orientation of gripper
     */
    public double getDeployOrientation() {
        return motor.getSelectedSensorPosition(0);
    }
}
