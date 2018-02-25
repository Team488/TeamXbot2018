package competition.subsystems.climberdeploy;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimberDeploySubsystem extends BaseSubsystem {

    final DoubleProperty deploySpeed;
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;
    public XCANTalon motor;

    @Inject
    public ClimberDeploySubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        deploySpeed = propMan.createPersistentProperty(getPrefix()+"Speed", .4);

        if (contract.climbDeployReady()) {
            initializeMotor();
        }
    }

    private void initializeMotor() {
        motor = clf.createCANTalon(contract.getClimbDeployMaster().channel);
        motor.setInverted(contract.getClimbDeployMaster().inverted);
    }

    /**
     * extends the climber arm
     */
    public void extendClimberArm() {
        motor.simpleSet(deploySpeed.get());
    }

    /**
     * detracts the climber arm
     */
    public void retractClimberArm() {
        motor.simpleSet(-deploySpeed.get());
    }

    /**
     * stops arm from moving or deploying
     */
    public void stopClimberArm() {
        motor.simpleSet(0);
    }

    /**
     * sensor to determine when the climber arm has deployed to the correct height
     */
    public boolean hitBarHeight() {
        return false;
    }

    /**
     * sensor to determine when the climber arm is fully retracted
     */
    public boolean isRetracted() {
        return false;
    }
    
    /**
     * returns current speed of the motor for the climber.
     */
    public double getCurrentSpeed() {
        return motor.getMotorOutputPercent();
    }
}
