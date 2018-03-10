package competition.operator_interface;

import javax.inject.Singleton;

import com.google.inject.Inject;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.PeriodicDataSource;
import xbot.common.controls.sensors.XJoystick;

@Singleton
public class RumbleManager implements PeriodicDataSource {
    private double lastDriverRequestEndTime = -1;
    private XJoystick driverGamepad;
    
    @Inject
    public RumbleManager(OperatorInterface oi) {
        this.driverGamepad = oi.driverGamepad;
    }
    
    public void stopDriverGamepadRumble() {
        writeRumble(driverGamepad, 0);
        lastDriverRequestEndTime = -1;
    }
    
    public void rumbleDriverGamepad(double intensity, double length) {
        writeRumble(driverGamepad, intensity);
        lastDriverRequestEndTime = Timer.getFPGATimestamp() + length;
    }
    
    private void writeRumble(XJoystick gamepad, double intensity) {

        GenericHID internalJoystick = gamepad.getRawWPILibJoystick();
        
        if (internalJoystick == null) {
            return;
        }

        internalJoystick.setRumble(RumbleType.kLeftRumble, intensity);
        internalJoystick.setRumble(RumbleType.kRightRumble, intensity);
    }

    @Override
    public void updatePeriodicData() {
        if (lastDriverRequestEndTime > 0 && Timer.getFPGATimestamp() > lastDriverRequestEndTime) {
            writeRumble(driverGamepad, 0);
            lastDriverRequestEndTime = -1;
        }
    }

    @Override
    public String getName() {
        return "RumbleManager";
    }
}
