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

    double currentDeploySpeed;
    final DoubleProperty fastDeploySpeed;
    final DoubleProperty slowDeploySpeed;
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;
    public XCANTalon motor;

    @Inject
    public ClimberDeploySubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        fastDeploySpeed = propMan.createPersistentProperty("fastDeploySpeed", .4);
        slowDeploySpeed = propMan.createPersistentProperty("slowDeploySpeed", .1);
        currentDeploySpeed = fastDeploySpeed.get();

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
        motor.simpleSet(currentDeploySpeed);
    }

    /**
     * detracts the climber arm
     */
    public void retractClimberArm() {
        motor.simpleSet(-currentDeploySpeed);
    }

    /**
     * stops arm from moving or deploying
     */
    public void stopClimberArm() {
        motor.simpleSet(0);
    }

    /**
     * speeds up the arm, regardless of what direction the arm is moving
     */
    public void increaseSpeed() {
        currentDeploySpeed = fastDeploySpeed.get();
    }

    /**
     * slows down the arm, regardless of what direction the arm is moving
     */
    public void decreaseSpeed() {
        currentDeploySpeed = slowDeploySpeed.get();
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
}
