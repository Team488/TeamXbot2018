package competition.subsystems.pose;

import com.google.inject.Inject;

import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

public class PoseSubsystem extends BasePoseSubsystem {

	@Inject
	public PoseSubsystem(CommonLibFactory factory, XPropertyManager propManager) {
		super(factory, propManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected double getLeftDriveDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double getRightDriveDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

}
