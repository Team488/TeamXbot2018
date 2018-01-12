package competition.subsystems.drive;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.drive.BaseDriveSubsystem;

@Singleton
public class DriveSubsystem extends BaseDriveSubsystem{
    private static Logger log = Logger.getLogger(DriveSubsystem.class);

    public final XCANTalon leftMaster;
    public final XCANTalon leftFollower;
    public final XCANTalon rightMaster;
    public final XCANTalon rightFollower;

    @Inject
    public DriveSubsystem(CommonLibFactory factory, XPropertyManager propManager) {
        log.info("Creating DriveSubsystem");

        this.leftMaster = factory.createCANTalon(34);
        this.leftFollower = factory.createCANTalon(35);
        leftFollower.follow(leftMaster);

        this.rightMaster = factory.createCANTalon(21);
        this.rightFollower = factory.createCANTalon(20);
        rightFollower.follow(rightMaster);
        
        this.rightMaster.setInverted(true);
    }

	@Override
	protected Map<XCANTalon, MotionRegistration> getAllMasterTalons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getLeftTotalDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRightTotalDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTransverseDistance() {
		// TODO Auto-generated method stub
		return 0;
	}
}
