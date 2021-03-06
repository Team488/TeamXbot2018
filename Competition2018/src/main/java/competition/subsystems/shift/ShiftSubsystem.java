package competition.subsystems.shift;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XSolenoid;
import xbot.common.injection.wpi_factories.CommonLibFactory;

@Singleton
public class ShiftSubsystem extends BaseSubsystem {
    public final XSolenoid solenoid;
    public Gear gear;

    public enum Gear {
        LOW_GEAR, HIGH_GEAR
    }

    @Inject
    public ShiftSubsystem(CommonLibFactory factory, ElectricalContract2018 contract) {
        this.solenoid = factory.createSolenoid(contract.getShifterSolenoid().channel);
        gear = Gear.LOW_GEAR;
    }

    public void setGear(Gear gear) {
        if (gear == Gear.HIGH_GEAR) {
            solenoid.setOn(true);
        } else {
            solenoid.setOn(false);
        }
        this.gear = gear;
    }

    public Gear getGear() {
        return gear;
    }
}
