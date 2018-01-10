package competition.subsystems.drive;

import org.apache.log4j.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.properties.XPropertyManager;

@Singleton
public class DriveSubsystem extends BaseSubsystem {
    private static Logger log = Logger.getLogger(DriveSubsystem.class);

    public final XCANTalon leftFrontDrive;
    public final XCANTalon leftRearDrive;
    public final XCANTalon rightFrontDrive;
    public final XCANTalon rightRearDrive;

    @Inject
    public DriveSubsystem(CommonLibFactory factory, XPropertyManager propManager) {
        log.info("Creating DriveSubsystem");

        this.leftFrontDrive = factory.createCANTalon(0);
        this.leftRearDrive = factory.createCANTalon(2);

        this.rightFrontDrive = factory.createCANTalon(1);
        this.rightFrontDrive.setInverted(true);
        this.rightRearDrive = factory.createCANTalon(3);
        this.rightRearDrive.setInverted(true);
    }

    public void tankDrive(double leftPower, double rightPower) {
        this.leftFrontDrive.set(ControlMode.PercentOutput, leftPower);
        this.leftRearDrive.set(ControlMode.PercentOutput, leftPower);
        this.rightFrontDrive.set(ControlMode.PercentOutput, rightPower);
        this.rightRearDrive.set(ControlMode.PercentOutput, rightPower);
    }
}
