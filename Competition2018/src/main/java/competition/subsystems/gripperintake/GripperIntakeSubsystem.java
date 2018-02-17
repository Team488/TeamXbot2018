package competition.subsystems.gripperintake;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class GripperIntakeSubsystem extends BaseSubsystem {

    final CommonLibFactory clf;
    final ElectricalContract2018 contract;
    public XCANTalon rightMotor;
    public XCANTalon leftMotor;

    final DoubleProperty power;

    @Inject
    public GripperIntakeSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        power = propMan.createPersistentProperty("Gripper Intake Power", 1);

        if (contract.collectorReady()) {
            initializeMotors();
        }
    }

    private void initializeMotors() {
        rightMotor = clf.createCANTalon(contract.getLeftCollectorMaster().channel);
        rightMotor.setInverted(contract.getLeftCollectorMaster().inverted);

        leftMotor = clf.createCANTalon(contract.getRightCollectorMaster().channel);
        leftMotor.setInverted(contract.getRightCollectorMaster().inverted);
    }

    /**
     * Directly controls motor power
     * 
     * @param power
     *            -1 intakes, +1 ejects
     */
    public void setPower(double rightPower, double leftPower) {
        rightMotor.simpleSet(rightPower);
        leftMotor.simpleSet(leftPower);
    }

    public void eject() {
        rightMotor.simpleSet(power.get());
        leftMotor.simpleSet(power.get());
    }

    public void intake() {
        rightMotor.simpleSet(power.get() * -1);
        leftMotor.simpleSet(power.get() * -1);
    }
    
    public void rotateClockwise() {
        rightMotor.simpleSet(power.get());
        leftMotor.simpleSet(power.get() * -1);
    }
    
    public void rotateCounterClockwise() {
        rightMotor.simpleSet(power.get() * -1);
        leftMotor.simpleSet(power.get());
    }

    public void stop() {
        rightMotor.simpleSet(0);
        leftMotor.simpleSet(0);
    }
}
