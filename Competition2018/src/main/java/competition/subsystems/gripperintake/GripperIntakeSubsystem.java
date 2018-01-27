package competition.subsystems.gripperintake;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class GripperIntakeSubsystem extends BaseSubsystem {
    
    CommonLibFactory clf;
    public XCANTalon rightMotor;
    public XCANTalon leftMotor;
    
    
    final DoubleProperty highPower;
    final DoubleProperty lowPower;
    
    @Inject
    public GripperIntakeSubsystem(CommonLibFactory clf, XPropertyManager propMan) {
        this.clf = clf;
        highPower = propMan.createPersistentProperty("Gripper Intake High Power", 1);
        lowPower = propMan.createPersistentProperty("Gripper Intake Low Power", 0.25);
    }
    
    public void temporaryHack() {
        rightMotor = clf.createCANTalon(34);
        leftMotor = clf.createCANTalon(31);
    }
    
    /**
     * Directly controls motor power
     * @param power -1 intakes, +1 ejects
     */
    public void setPower(double rightPower, double leftPower) {
        rightMotor.simpleSet(rightPower);
        leftMotor.simpleSet(leftPower);
    }
    
    public void eject() {
        rightMotor.simpleSet(highPower.get());
        leftMotor.simpleSet(highPower.get());
    }
    
    public void intake() {
        rightMotor.simpleSet(highPower.get()*-1);
        leftMotor.simpleSet(highPower.get()*-1);
    }
    
    public void intakeleftDominant () {
        rightMotor.simpleSet(lowPower.get()*-1);
        leftMotor.simpleSet(highPower.get()*-1);
    }
    
    public void intakerightDominant() {
        rightMotor.simpleSet(highPower.get()*-1);
        leftMotor.simpleSet(lowPower.get()*-1);
    }
    
    public void stop() {
        rightMotor.simpleSet(0);
        leftMotor.simpleSet(0);
    }
}
