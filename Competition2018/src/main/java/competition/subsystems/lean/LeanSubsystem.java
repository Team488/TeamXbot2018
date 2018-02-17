package competition.subsystems.lean;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;

@Singleton
public class LeanSubsystem extends BaseSubsystem {

    final CommonLibFactory clf;
    final ElectricalContract2018 contract;

    public XCANTalon motor;

    @Inject
    public LeanSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;

        if (contract.climbLeanReady()) {
            initializeMotor();
        }
    }

    private void initializeMotor() {
        motor = clf.createCANTalon(contract.getClimbLeanMaster().channel);
        motor.setInverted(contract.getClimbLeanMaster().inverted);
    }

    public void setPower(double power) {
        motor.simpleSet(power);
    }

    public double getPower() {
        return motor.getMotorOutputPercent();
    }
}
