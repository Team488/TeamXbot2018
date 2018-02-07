package competition.subsystems.drive;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
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

    private Map<XCANTalon, MotionRegistration> masterTalons;

    public enum Side {
        Left, Right
    }

    @Inject
    public DriveSubsystem(CommonLibFactory factory, XPropertyManager propManager, ElectricalContract2018 contract) {
        log.info("Creating DriveSubsystem");

        this.leftMaster = factory.createCANTalon(contract.getLeftDriveMaster().channel);
        this.leftFollower = factory.createCANTalon(contract.getLeftDriveFollower().channel);
        configureMotorTeam(
                "LeftDriveMaster",
                leftMaster, leftFollower,
                contract.getLeftDriveMaster().inverted, contract.getLeftDriveFollower().inverted,
                true);

        this.rightMaster = factory.createCANTalon(contract.getRightDriveMaster().channel);
        this.rightFollower = factory.createCANTalon(contract.getRightDriveFollower().channel);
        configureMotorTeam(
                "RightDriveMaster",
                rightMaster, rightFollower,
                contract.getRightDriveMaster().inverted, contract.getRightDriveFollower().inverted,
                true);

        masterTalons = new HashMap<XCANTalon, BaseDriveSubsystem.MotionRegistration>();
        masterTalons.put(leftMaster, new MotionRegistration(0, 1, -1));
        masterTalons.put(rightMaster, new MotionRegistration(0, 1, 1));

        leftMaster.createTelemetryProperties("LeftDriveMaster");
        rightMaster.createTelemetryProperties("RightDriveMaster");

        leftTicksPerFiveFeet = propManager.createPersistentProperty("leftDriveTicksPer5Feet", 0);
        rightTicksPerFiveFeet = propManager.createPersistentProperty("rightDriveTicksPer5Feet", 0);
    }
    
    private static void configureMotorTeam(
            String masterName,
            XCANTalon master, XCANTalon follower,
            boolean masterInverted, boolean followerInverted,
            boolean sensorPhase) {
        follower.follow(master);

        master.setInverted(masterInverted);
        follower.setInverted(followerInverted);
        
        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        master.setSensorPhase(sensorPhase);
        master.createTelemetryProperties(masterName);
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
}
