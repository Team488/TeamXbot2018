package competition.subsystems.wrist;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseSetpointSubsystem;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;

@Singleton
public class WristSubsystem extends BaseSetpointSubsystem implements PeriodicDataSource {
    
    final CommonLibFactory clf;
    final ElectricalContract2018 contract;
    public XSolenoid wristSolenoid;

    @Inject
    WristSubsystem(CommonLibFactory clf, ElevatorSubsystem elevator, XPropertyManager propMan,
            ElectricalContract2018 contract) {
        this.clf = clf;
        this.contract = contract;
        
        if (contract.wristReady()) {
            initializeComponents();
        }
    }

    public boolean getUp() {
        return wristSolenoid.getAdjusted();
    }

    private void initializeComponents() {
        wristSolenoid = clf.createSolenoid(contract.getWristMaster().channel);
        wristSolenoid.setInverted(contract.getWristMaster().inverted);
    }
    
    public void setWristUp(boolean up) {
        wristSolenoid.setOn(up);
    }


    /**
     * angles the Gripper up
     */
    public void goUp() {
        setWristUp(true);
    }

    /**
     * angles the Gripper down
     */
    public void goDown() {
        setWristUp(false);
    }
    @Override
    public void updatePeriodicData() {   
    }
}
