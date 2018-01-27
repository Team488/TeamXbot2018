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

    final DoubleProperty highPower;
    final DoubleProperty lowPower;

    @Inject
    public GripperIntakeSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        highPower = propMan.createPersistentProperty("Gripper Intake High Power", 1);
        lowPower = propMan.createPersistentProperty("Gripper Intake Low Power", 0.25);

        if (contract.collectorReady()) {
            temporaryHack();
        }
    }

    public void temporaryHack() {
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
        rightMotor.simpleSet(highPower.get());
        leftMotor.simpleSet(highPower.get());
    }

    public void intake() {
        rightMotor.simpleSet(highPower.get() * -1);
        leftMotor.simpleSet(highPower.get() * -1);
    }

    public void intakeleftDominant() {
        rightMotor.simpleSet(lowPower.get() * -1);
        leftMotor.simpleSet(highPower.get() * -1);
    }

    public void intakerightDominant() {
        rightMotor.simpleSet(highPower.get() * -1);
        leftMotor.simpleSet(lowPower.get() * -1);
    }

    public void stop() {
        rightMotor.simpleSet(0);
        leftMotor.simpleSet(0);
    }
}
