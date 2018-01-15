package competition.subsystems.drive;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

@Singleton
public class DriveSubsystem extends BaseDriveSubsystem{
    private static Logger log = Logger.getLogger(DriveSubsystem.class);

    public final XCANTalon leftMaster;
    public final XCANTalon leftFollower;
    public final XCANTalon rightMaster;
    public final XCANTalon rightFollower;
    
    private final DoubleProperty leftTicksPerFiveFeet;
    private final DoubleProperty rightTicksPerFiveFeet;
    
    private Map<XCANTalon, MotionRegistration> masterTalons;

    @Inject
    public DriveSubsystem(CommonLibFactory factory, XPropertyManager propManager) {
        log.info("Creating DriveSubsystem");

        this.leftMaster = factory.createCANTalon(34);
        this.leftFollower = factory.createCANTalon(35);
        leftFollower.follow(leftMaster);
        
        leftMaster.setInverted(true);
        leftFollower.setInverted(true);

        this.rightMaster = factory.createCANTalon(21);
        this.rightFollower = factory.createCANTalon(20);
        rightFollower.follow(rightMaster);
        rightFollower.setInverted(true);
        
        this.rightMaster.setInverted(true);
        masterTalons = new HashMap<XCANTalon, BaseDriveSubsystem.MotionRegistration>();
        masterTalons.put(leftMaster, new MotionRegistration(0, 1, -1));
        masterTalons.put(rightMaster, new MotionRegistration(0, 1, 1));
        
        
        leftMaster.createTelemetryProperties("LeftDriveMaster");
        rightMaster.createTelemetryProperties("RightDriveMaster");
        
        leftTicksPerFiveFeet = propManager.createPersistentProperty("leftDriveTicksPer5Feet", 0);
        rightTicksPerFiveFeet = propManager.createPersistentProperty("rightDriveTicksPer5Feet", 0);
        
    }
    
    public double ticksToInches(double ticks) {
        double tpi = ticksPerInch();
        
        if (tpi != 0) {
            return (1.0 / ticksPerInch()) * ticks;
        }
        return 0;
    }
    
    public double inchesToTicks(double inches) {
        // this is taking the average of left and right, then dividing by 60 inches (5 feet)
        return ticksPerInch() * inches;
    }
    
    private double ticksPerInch() {
        return ((leftTicksPerFiveFeet.get() + rightTicksPerFiveFeet.get()) / 120.0);
    }

	@Override
	protected Map<XCANTalon, MotionRegistration> getAllMasterTalons() {
		return masterTalons;
	}

	@Override
	public double getLeftTotalDistance() {
		return ticksToInches(leftMaster.getSelectedSensorPosition(0));
	}

	@Override
	public double getRightTotalDistance() {
		return ticksToInches(rightMaster.getSelectedSensorPosition(0));
	}

	@Override
	public double getTransverseDistance() {
		// TODO Auto-generated method stub
		return 0;
	}
}
