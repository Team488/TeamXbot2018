package competition.subsystems.elevator.commands;

import xbot.common.command.BaseCommand;
import xbot.common.math.PIDFactory;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;

import com.google.inject.Inject;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;
import competition.operator_interface.OperatorInterface;

public class ElevatorMaintainerCommand extends BaseCommand {

    OperatorInterface oi;
    ElevatorSubsystem elevator;
    PIDManager pid;
    double startTime;
    double attemptTime;
    final DoubleProperty calibrationAttemptTime;

    @Inject
    public ElevatorMaintainerCommand(
            ElevatorSubsystem elevator, 
            PIDFactory pf, 
            XPropertyManager propMan, 
            OperatorInterface oi) {
        calibrationAttemptTime = propMan.createPersistentProperty("Calibration attempt time (ms)", 4000);
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
            startTime = Timer.getFPGATimestamp();
            attemptTime = startTime + calibrationAttemptTime.get();
        }
    }

    @Override
    public void execute() {
        boolean maintain = false;
        boolean manual = false;
        boolean tryToCalibrate = false;
        if (elevator.isCalibrated()) {
            maintain = true;
        }
        else if (Timer.getFPGATimestamp() < attemptTime) {
            tryToCalibrate = true;
        }
        else {
            manual = true;
        }
        
        
        
        if (maintain) {
            elevator.setPower(pid.calculate(elevator.getTargetHeight(), elevator.getCurrentHeightInInches()));
        }
        else if (tryToCalibrate) {
            elevator.setPower(-1);
        }
        else if (manual) {
            elevator.setPower(oi.operatorGamepad.getRightStickY());
        }
    }  
}
