package competition.subsystems.pose;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.autonomous.AutonomousPathSupplier.StartingLocations;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;
import xbot.common.subsystems.pose.BasePoseSubsystem;

@Singleton
public class PoseSubsystem extends BasePoseSubsystem {

    DriveSubsystem drive;

    @Inject
    public PoseSubsystem(CommonLibFactory factory, XPropertyManager propManager, DriveSubsystem drive) {
        super(factory, propManager);
        this.drive = drive;

        updatePeriodicData();
        resetDistanceTraveled();
    }

    @Override
    protected double getLeftDriveDistance() {
        return drive.getLeftTotalDistance();
    }

    @Override
    protected double getRightDriveDistance() {
        return drive.getRightTotalDistance();
    }
    
    public void initializePoseForStartLocation(StartingLocations startLocation) {
        log.info("Initializing pose for start location: " + startLocation);
        switch (startLocation) {
        case Left:
            setCurrentPosition(46.75, 38.5);
            setCurrentHeading(90);
            break;
        case Middle:
            setCurrentPosition(166.75, 38.5);
            setCurrentHeading(90);
            break;
        case Right:
            setCurrentPosition(277.25, 38.5);
            setCurrentHeading(90);
            break;
        default:
            break;
        }
    }

}
