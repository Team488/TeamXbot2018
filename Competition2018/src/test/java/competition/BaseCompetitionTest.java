package competition;

import org.junit.Ignore;

import competition.subsystems.offboard.MockOffboardCommsInterface;
import competition.subsystems.offboard.XOffboardCommsInterface;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.pose.PoseSubsystem;
import xbot.common.injection.BaseWPITest;
import xbot.common.injection.UnitTestModule;
import xbot.common.subsystems.drive.BaseDriveSubsystem;
import xbot.common.subsystems.drive.MockDriveSubsystem;
import xbot.common.subsystems.pose.BasePoseSubsystem;
import xbot.common.subsystems.pose.TestPoseSubsystem;

@Ignore
public class BaseCompetitionTest extends BaseWPITest {

    protected OperatorInterface oi;
    
    protected class TestModule extends UnitTestModule {
        @Override
        protected void configure() {
            super.configure();

            this.bind(ElectricalContract2018.class).to(ImaginaryRobot2018.class);
            
            this.bind(BasePoseSubsystem.class).to(PoseSubsystem.class);
            this.bind(BaseDriveSubsystem.class).to(DriveSubsystem.class);
            this.bind(XOffboardCommsInterface.class).to(MockOffboardCommsInterface.class);
        }
    }

    public BaseCompetitionTest() {
        guiceModule = new TestModule();
    }

    @Override
    public void setUp() {
        super.setUp();
        
        oi = injector.getInstance(OperatorInterface.class);
    }
}
