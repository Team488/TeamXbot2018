package competition.subsystems.drive;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import competition.subsystems.power_state_manager.PowerStateManagerSubsystem;
import competition.subsystems.power_state_manager.PowerStateResponsiveController;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

@Singleton
public class DriveSubsystem extends BaseDriveSubsystem implements PowerStateResponsiveController {
    private static Logger log = Logger.getLogger(DriveSubsystem.class);

    public final XCANTalon leftMaster;
    public final XCANTalon leftFollower;
    public final XCANTalon rightMaster;
    public final XCANTalon rightFollower;

    private final DoubleProperty leftTicksPerFiveFeet;
    private final DoubleProperty rightTicksPerFiveFeet;

    private final PIDManager leftVelocityPidManager;
    private final PIDManager rightVelocityPidManager;

    private Map<XCANTalon, MotionRegistration> masterTalons;
    
    private final PIDManager positionalPid;
    private final PIDManager rotateToHeadingPid;
    private final PIDManager rotateDecayPid;
    
    private final PIDManager rotationalVelocityPid;
    private final PIDManager translationalVelocityPid;

    private double leftAccum;
    private double rightAccum;
    
    private final DoubleProperty voltageRampNormalProp;
    private final DoubleProperty voltageRampLowBatProp;
    private final DoubleProperty maxCurrentNormalProp;
    private final DoubleProperty maxCurrentLowBatProp;

    public enum Side {
        Left, Right
    }

    @Inject
    public DriveSubsystem(
            CommonLibFactory factory,
            PowerStateManagerSubsystem powerStateManager,
            XPropertyManager propManager,
            ElectricalContract2018 contract,
            PIDFactory pf) {
        log.info("Creating DriveSubsystem");
        
        positionalPid = pf.createPIDManager(getPrefix()+"Drive to position", 0.1, 0, 0, 0, 0.75, -0.75, 3, 1, 0.5);
        rotateToHeadingPid = pf.createPIDManager(getPrefix()+"DriveHeading", 4, 0, 0);
        rotateDecayPid = pf.createPIDManager(getPrefix()+"DriveDecay", 0, 0, 1);
        
        rotationalVelocityPid = pf.createPIDManager(getPrefix() + "RotationalVelocity", 0.001, 0, 0);
        translationalVelocityPid = pf.createPIDManager(getPrefix() + "TranslationalVelocity", 0.01, 0, 0);

        voltageRampNormalProp = propManager.createPersistentProperty(getPrefix() + "Voltage ramp time (normal)", 0.15);
        voltageRampLowBatProp = propManager.createPersistentProperty(getPrefix() + "Voltage ramp time (low battery)", 0.5);
        maxCurrentNormalProp = propManager.createPersistentProperty(getPrefix() + "Current limit (normal)", 0);
        maxCurrentLowBatProp = propManager.createPersistentProperty(getPrefix() + "Current limit (low-battery)", 10);

        // Default is for 2018 robot design
        // SRX counts edges rather than ticks, so the 1024-count sensor is read as 4096 per rev
        // (4096 native units/encoder rev) * (3 encoder rev/wheel rev) / (4pi inches/wheel rev) ~= 977.847970356605
        final double defaultDriveTicksPerInch = 4096d * 3d / (Math.PI * 4d);
        final double defaultDriveTicksPer5Feet = defaultDriveTicksPerInch * (12 * 5);
        leftTicksPerFiveFeet = propManager.createPersistentProperty(getPrefix()+"leftDriveTicksPer5Feet",
                100281);
        rightTicksPerFiveFeet = propManager.createPersistentProperty(getPrefix()+"rightDriveTicksPer5Feet",
                100281);

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
        
        this.leftVelocityPidManager = pf.createPIDManager(getPrefix()+"Velocity (local)", 0.005, 0, 0.01, 0, 1, -1);
        this.rightVelocityPidManager = pf.createPIDManager(getPrefix()+"Velocity (local)", 0.005, 0, 0.01, 0, 1, -1);

        this.setVoltageRamp(voltageRampNormalProp.get());
        this.setCurrentLimits(0, false);
        powerStateManager.registerResponsiveController(this);
    }

    private void configureMotorTeam(String masterName, XCANTalon master, XCANTalon follower, boolean masterInverted,
            boolean followerInverted, boolean sensorPhase) {
        follower.follow(master);

        master.setInverted(masterInverted);
        follower.setInverted(followerInverted);

        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        master.setSensorPhase(sensorPhase);
        master.createTelemetryProperties(getPrefix(), masterName);

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
    
    public void resetVelocityAccum() {
        this.leftAccum = 0;
        this.rightAccum = 0;
    }
    
    public void driveTankVelocity(double leftInchesPerSecond, double rightInchesPerSecond) {
        leftAccum += this.leftVelocityPidManager.calculate(leftInchesPerSecond, getVelocity(Side.Left));
        rightAccum += this.rightVelocityPidManager.calculate(rightInchesPerSecond, getVelocity(Side.Right));

        leftAccum = MathUtils.constrainDoubleToRobotScale(leftAccum);
        rightAccum = MathUtils.constrainDoubleToRobotScale(rightAccum);
        
        this.drive(leftAccum, rightAccum);
    }

    @Override
    public void updatePeriodicData() {
        super.updatePeriodicData();
    }

    public PIDManager getPositionalPid() {
        return positionalPid;
    }

    public PIDManager getRotateToHeadingPid() {
        return rotateToHeadingPid;
    }

    public PIDManager getRotateDecayPid() {
        return rotateDecayPid;
    }
    
    public PIDManager getRotationalVelocityPid() {
        return rotationalVelocityPid;
    }
    
    public PIDManager getTranslationalVelocityPid() {
        return translationalVelocityPid;
    }
    
    public double getLeftTicksPerFiveFt() {
        return leftTicksPerFiveFeet.get();
    }
    
    public double getRightTicksPerFiveFt() {
        return rightTicksPerFiveFeet.get();
    }
    
    public double getVelocityInInchesPerSecond() {
        double leftSpeed = ticksToInches(Side.Left, leftMaster.getSelectedSensorVelocity(0)) * 10;
        double rightSpeed = ticksToInches(Side.Right, rightMaster.getSelectedSensorVelocity(0)) * 10;
        return (leftSpeed + rightSpeed) / 2;
    }
    
    private void setCurrentLimitsForLowBatMode(boolean isLowBatMode) {
        if (isLowBatMode) {
            this.setCurrentLimits((int)maxCurrentLowBatProp.get(), true);
        }
        else {
            int maxCurrent = (int)maxCurrentNormalProp.get();
            // Setting the normal current limit to 0 will disable current limiting
            if (maxCurrent > 0) {
                this.setCurrentLimits(maxCurrent, true);
            }
            else {
                this.setCurrentLimits(0, false);
            }
        }
    }
    
    @Override
    public void onEnterLowBatteryMode() {
        setCurrentLimitsForLowBatMode(true);
        this.setVoltageRamp(voltageRampLowBatProp.get());
    }

    @Override
    public void onLeaveLowBatteryMode() {
        this.setCurrentLimitsForLowBatMode(false);
        this.setVoltageRamp(voltageRampNormalProp.get());
    }
}
