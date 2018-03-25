package competition.subsystems.wrist.commands;

import competition.subsystems.wrist.WristSubsystem;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class SetWristTo90Degrees extends SetWristAngleCommand{
    
    final DoubleProperty wristUpAngle;
    final DoubleProperty angleDeviationLimit;
    WristSubsystem wrist;

    public SetWristTo90Degrees(WristSubsystem wrist, XPropertyManager propMan) {
        super(wrist);
        this.wrist = wrist;
        wristUpAngle = propMan.createPersistentProperty("Wrist Up Angle", 90);
        angleDeviationLimit = propMan.createPersistentProperty("Angle Devation Limit", 10);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        log.info("Setting Wrist angle to " + wristUpAngle.get());
        wrist.setTargetAngle(wristUpAngle.get());
    }

    @Override
    public void execute() {
        wrist.setTargetAngle(wristUpAngle.get());
    }
    
    public boolean isFinished() {
        return wrist.getWristAngle() > 90 - angleDeviationLimit.get() 
                && wrist.getWristAngle() < 90 + angleDeviationLimit.get();
    }

}
