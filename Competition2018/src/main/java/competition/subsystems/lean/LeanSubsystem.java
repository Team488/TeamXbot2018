package competition.subsystems.lean;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class LeanSubsystem extends BaseSubsystem {

    double currentLeanSpeed;
    final DoubleProperty fastLeanSpeed;
    final DoubleProperty slowLeanSpeed;
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;

    public XCANTalon motor;

    @Inject
    public LeanSubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        slowLeanSpeed = propMan.createPersistentProperty("slowLeanSpeed", .1);
        fastLeanSpeed = propMan.createPersistentProperty("fastLeanSpeed", .4);
        currentLeanSpeed = fastLeanSpeed.get();

        if (contract.climbLeanReady()) {
            initializeMotor();
        }
    }

    private void initializeMotor() {
        motor = clf.createCANTalon(contract.getClimbLeanMaster().channel);
        motor.setInverted(contract.getClimbLeanMaster().inverted);
    }

    /**
     * makes the climb arm lean left
     */
    public void leanLeft() {
        motor.simpleSet(currentLeanSpeed);
    }

    /**
     * makes the climb arm lean right
     */
    public void leanRight() {
        motor.simpleSet(-currentLeanSpeed);
    }

    /**
     * moves the arm to center position
     */
    public void center() {

    }

    /**
     * stops the climb arm from leaning left
     */
    public void stopLean() {
        motor.simpleSet(0);
    }

    /**
     * sensor to determine if the arm has hit the bar
     */
    public boolean hitBar() {
        return false;
    }

    /**
     * increases leaning arm speed
     */
    public void increaseSpeed() {
        currentLeanSpeed = fastLeanSpeed.get();
    }

    /**
     * decreases leaning arm speed
     */
    public void decreaseSpeed() {
        currentLeanSpeed = slowLeanSpeed.get();
    }

    public void setLeanSpeed(double power) {
        currentLeanSpeed = power;
    }
}
