package competition.subsystems.elevator.commands;

import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;

import com.google.inject.Inject;
import xbot.common.math.PIDManager;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.operator_interface.OperatorInterface;

public class ElevatorMaintainerCommand extends BaseCommand {
    
    public enum MaintinerMode {
        Calibrating,
        GaveUp,
        Calibrated
    }

    OperatorInterface oi;
    ElevatorSubsystem elevator;
    PIDManager pid;
    double giveUpCalibratingTime;
    final BooleanProperty motionMagic;
    final DoubleProperty elevatorCalibrationAttemptTimeMS;

    @Inject
    public ElevatorMaintainerCommand(
            ElevatorSubsystem elevator, 
            PIDFactory pf, 
            XPropertyManager propMan, 
            OperatorInterface oi) {
        elevatorCalibrationAttemptTimeMS = propMan.createPersistentProperty("Calibration attempt time (ms)", 4000);
        motionMagic = propMan.createPersistentProperty("Motion Magic Mode", false);
        this.elevator = elevator;
        this.requires(elevator);
        this.oi = oi;
        pid = pf.createPIDManager("Elevator", 1, 0, 0);
        pid.setErrorThreshold(0.1);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        if (!elevator.isCalibrated()) {
            log.warn("ELEVATOR UNCALIBRATED - THIS COMMAND WILL NOT DO ANYTHING!");
            giveUpCalibratingTime = Timer.getFPGATimestamp() + elevatorCalibrationAttemptTimeMS.get();
            log.info("Attempting calibration from " + Timer.getFPGATimestamp() + " until " + giveUpCalibratingTime);
        }
    }

    @Override
    public void execute() {
        MaintinerMode currentMode = MaintinerMode.Calibrating;
        
        if (elevator.isCalibrated()) {
            currentMode = MaintinerMode.Calibrated;
        }
        else if (Timer.getFPGATimestamp() < giveUpCalibratingTime) {
            currentMode = MaintinerMode.Calibrating;
        }
        else {
            currentMode = MaintinerMode.GaveUp;
        }
        
        
        
        if (currentMode == MaintinerMode.Calibrated) {
            if (motionMagic.get() == true) {
                elevator.motionMagicToHeight(elevator.getTargetHeight());
            } else {
                elevator.setPower(pid.calculate(elevator.getTargetHeight(), elevator.getCurrentHeightInInches()));
            }            
        }
        else if (currentMode == MaintinerMode.Calibrating) {
            elevator.lower();
        }
        else if (currentMode == MaintinerMode.GaveUp) {
            elevator.setPower(oi.operatorGamepad.getRightStickY());
        }
    }  
    
    public void isMotionMagicMode(boolean x) {
        motionMagic.set(x);
    }
}
