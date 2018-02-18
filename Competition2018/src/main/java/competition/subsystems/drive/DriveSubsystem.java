package competition.subsystems.drive;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

@Singleton
public class DriveSubsystem extends BaseDriveSubsystem {
    private static Logger log = Logger.getLogger(DriveSubsystem.class);

    public final XCANTalon leftMaster;
    public final XCANTalon leftFollower;
    public final XCANTalon rightMaster;
    public final XCANTalon rightFollower;

    private final DoubleProperty leftTicksPerFiveFeet;
    private final DoubleProperty rightTicksPerFiveFeet;

    private final DoubleProperty velocityP;
    private final DoubleProperty velocityI;
    private final DoubleProperty velocityD;
    private final DoubleProperty velocityF;

    private Map<XCANTalon, MotionRegistration> masterTalons;
    
    private final PIDManager positionalPid;
    private final PIDManager rotateToHeadingPid;
    private final PIDManager rotateDecayPid;
    
    int updateMotorValuesCounter = 0;

    public enum Side {
        Left, Right
    }

    @Inject
    public DriveSubsystem(CommonLibFactory factory, XPropertyManager propManager, ElectricalContract2018 contract, PIDFactory pf) {
        log.info("Creating DriveSubsystem");
        
        positionalPid = pf.createPIDManager(getPrefix()+"Drive to position", 0.1, 0, 0, 0, 0.5, -0.5, 3, 1, 0.5);
        rotateToHeadingPid = pf.createPIDManager(getPrefix()+"DriveHeading", 4, 0, 0);
        rotateDecayPid = pf.createPIDManager(getPrefix()+"DriveDecay", 0, 0, 1);
        

        // Default is for 2018 robot design
        // SRX counts edges rather than ticks, so the 1024-count sensor is read as 4096 per rev
        // (4096 native units/encoder rev) * (3 encoder rev/wheel rev) / (4pi inches/wheel rev) ~= 977.847970356605
        final double defaultDriveTicksPerInch = 4096d * 3d / (Math.PI * 4d);
        final double defaultDriveTicksPer5Feet = defaultDriveTicksPerInch * (12 * 5);
        leftTicksPerFiveFeet = propManager.createPersistentProperty(getPrefix()+"leftDriveTicksPer5Feet",
                defaultDriveTicksPer5Feet);
        rightTicksPerFiveFeet = propManager.createPersistentProperty(getPrefix()+"rightDriveTicksPer5Feet",
                defaultDriveTicksPer5Feet);

        velocityP = propManager.createPersistentProperty(getPrefix()+"Velocity control P", 0);
        velocityI = propManager.createPersistentProperty(getPrefix()+"Velocity control I", 0);
        velocityD = propManager.createPersistentProperty(getPrefix()+"Velocity control D", 0);
        velocityF = propManager.createPersistentProperty(getPrefix()+"Velocity control F", 0);

        this.leftMaster = factory.createCANTalon(contract.getLeftDriveMaster().channel);
        this.leftFollower = factory.createCANTalon(contract.getLeftDriveFollower().channel);
        configureMotorTeam("LeftDriveMaster", leftMaster, leftFollower, contract.getLeftDriveMaster().inverted,
                contract.getLeftDriveFollower().inverted, contract.getLeftDriveMasterEncoder().inverted);

        this.rightMaster = factory.createCANTalon(contract.getRightDriveMaster().channel);
        this.rightFollower = factory.createCANTalon(contract.getRightDriveFollower().channel);
        configureMotorTeam("RightDriveMaster", rightMaster, rightFollower, contract.getRightDriveMaster().inverted,
                contract.getRightDriveFollower().inverted, contract.getRightDriveMasterEncoder().inverted);

        masterTalons = new HashMap<XCANTalon, BaseDriveSubsystem.MotionRegistration>();
        masterTalons.put(leftMaster, new MotionRegistration(0, 1, -1));
        masterTalons.put(rightMaster, new MotionRegistration(0, 1, 1));
    }

    private void configureMotorTeam(String masterName, XCANTalon master, XCANTalon follower, boolean masterInverted,
            boolean followerInverted, boolean sensorPhase) {
        follower.follow(master);

        master.setInverted(masterInverted);
        follower.setInverted(followerInverted);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        master.setSensorPhase(sensorPhase);
        master.createTelemetryProperties(getPrefix(), masterName);

        this.updateMotorPidValues(master);

        // Master Config
        master.setNeutralMode(NeutralMode.Coast);
        master.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
        master.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);

        master.configPeakOutputForward(1, 0);
        master.configPeakOutputReverse(-1, 0);

        // Follower Config
        follower.setNeutralMode(NeutralMode.Coast);
        follower.configPeakOutputForward(1, 0);
        follower.configPeakOutputReverse(-1, 0);

        follower.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
    }

    private void updateMotorPidValues(XCANTalon motor) {
        motor.config_kP(0, this.velocityP.get(), 0);
        motor.config_kI(0, this.velocityI.get(), 0);
        motor.config_kD(0, this.velocityD.get(), 0);
        motor.config_kF(0, this.velocityF.get(), 0);
    }

    /**
     * Helper function to get the "typical" number of inches for a given number of ticks. This assumes that there isn't
     * much difference between left/right drive encoders.
     * 
     * @param ticks
     *            Number of encoder ticks
     * @return Number of inches
     */
    public double ticksToInches(Side side, double ticks) {
        double ticksPerInch = getSideTicksPerInch(side);

        // Escape if nobody ever defined any ticks per inch
        if (ticksPerInch == 0) {
            return 0;
        }
        return ticks / ticksPerInch;
    }

    /**
     * Helper function to get the typical number of ticks for a given number of inches. This assumes that there isn't
     * much difference between left/right drive encoders.
     * 
     * @param inches
     *            Number of inches
     * @return Number of encoder ticks
     */
    public double getInchesToTicks(Side side, double inches) {
        // this is taking the average of left and right, then dividing by 60 inches (5 feet)
        return getSideTicksPerInch(side) * inches;
    }

    public double getSideTicksPerInch(Side side) {
        switch (side) {
        case Left:
            return leftTicksPerFiveFeet.get() / 60;
        case Right:
            return rightTicksPerFiveFeet.get() / 60;
        default:
            return 0;
        }
    }

    /**
     * Returns the velocity of the specified side in inches per second.
     */
    public double getVelocity(Side side) {
        double motorNativeVelocity = (side == Side.Left ? leftMaster : rightMaster).getSelectedSensorVelocity(0);
        double nativeUnitsPerSecond = motorNativeVelocity * 10;
        return nativeUnitsPerSecond / this.getSideTicksPerInch(side);
    }

    @Override
    protected Map<XCANTalon, MotionRegistration> getAllMasterTalons() {
        return masterTalons;
    }

    @Override
    public double getLeftTotalDistance() {
        return ticksToInches(Side.Left, leftMaster.getSelectedSensorPosition(0));
    }

    @Override
    public double getRightTotalDistance() {
        return ticksToInches(Side.Right, rightMaster.getSelectedSensorPosition(0));
    }

    @Override
    public double getTransverseDistance() {
        return 0;
    }

    public void driveTankVelocity(double leftInchesPerSecond, double rightInchesPerSecond) {
        // Talon SRX measures in native units per 100ms, so values in seconds are divided by 10
        leftMaster.set(ControlMode.Velocity, getSideTicksPerInch(Side.Left) * leftInchesPerSecond / 10d);
        rightMaster.set(ControlMode.Velocity, getSideTicksPerInch(Side.Right) * rightInchesPerSecond / 10d);
    }

    @Override
    public void updatePeriodicData() {
        super.updatePeriodicData();

        updateMotorValuesCounter++;

        // roughly 5 seconds at 30 Hz
        if (updateMotorValuesCounter == 150) {
            updateMotorValuesCounter = 0;
            this.updateMotorPidValues(leftMaster);
            this.updateMotorPidValues(rightMaster);
        }
    }

    @Override
    public PIDManager getPositionalPid() {
        return positionalPid;
    }

    @Override
    public PIDManager getRotateToHeadingPid() {
        return rotateToHeadingPid;
    }

    @Override
    public PIDManager getRotateDecayPid() {
        return rotateDecayPid;
    }
}
