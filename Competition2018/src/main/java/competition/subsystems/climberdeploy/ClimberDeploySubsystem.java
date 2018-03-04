package competition.subsystems.climberdeploy;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class ClimberDeploySubsystem extends BaseSubsystem {

    final DoubleProperty deploySpeed;
    final DoubleProperty absoluteMaxTicks;
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;
    public XCANTalon motor;

    @Inject
    public ClimberDeploySubsystem(CommonLibFactory clf, XPropertyManager propMan, ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        deploySpeed = propMan.createPersistentProperty(getPrefix()+"Speed", .4);
        absoluteMaxTicks = propMan.createPersistentProperty(getPrefix() + "Absolute Max Ticks", 36000);
        if (contract.climbDeployReady()) {
            initializeMotor();
        }
    }

    private void initializeMotor() {
        motor = clf.createCANTalon(contract.getClimbDeployMaster().channel);
        motor.setInverted(contract.getClimbDeployMaster().inverted);
        
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    }
    
    public int getTicks() {
        return motor.getSelectedSensorPosition(0);
    }

    /**
     * extends the climber arm
     */
    public void extendClimberArm() {
        setPower(deploySpeed.get());
    }

    /**
     * detracts the climber arm
     */
    public void retractClimberArm() {
        setPower(-deploySpeed.get());
    }
    
    
    // Use this once we have a good understanding of limits
    public void setPower(double power) {
        /*if (getTicks() < 1000) {
            power = MathUtils.constrainDouble(power, 0, 1);
        }*/
        
        if (getTicks() > absoluteMaxTicks.get()) {
            power = MathUtils.constrainDouble(power, -1, 0);
        }        
        
        motor.simpleSet(power);
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
