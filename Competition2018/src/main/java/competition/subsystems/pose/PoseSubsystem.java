package competition.subsystems.pose;

import com.google.inject.Inject;

import competition.subsystems.drive.DriveSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class PoseSubsystem extends BasePoseSubsystem {
    
    DriveSubsystem drive;

	@Inject
	public PoseSubsystem(CommonLibFactory factory, XPropertyManager propManager, DriveSubsystem drive) {
		super(factory, propManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected double getLeftDriveDistance() {
		return drive.getLeftTotalDistance();
	}

	@Override
	protected double getRightDriveDistance() {
		return drive.getRightTotalDistance();
	}

}
