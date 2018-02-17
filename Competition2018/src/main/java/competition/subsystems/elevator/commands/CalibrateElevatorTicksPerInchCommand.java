package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.climb.ClimbSubsystem;
import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.wrist.WristSubsystem;
import xbot.common.command.BaseCommand;

public class CalibrateElevatorTicksPerInchCommand extends BaseCommand {

    int maxTick;
    int minTick;
    ElevatorSubsystem elevator;
    OperatorInterface oi;

    /**
     * This command needs to require all possible subsystems - while calibrating the elevator we DO NOT WANT ANYTHING
     * ELSE TO MOVE.
     */
    @Inject
    public CalibrateElevatorTicksPerInchCommand(ElevatorSubsystem elevator, DriveSubsystem drive,
            WristSubsystem wrist, GripperIntakeSubsystem intake, LeanSubsystem leaner, ClimbSubsystem climber,
            ClimberDeploySubsystem climbDeploy, OperatorInterface oi) {
        this.elevator = elevator;
        this.oi = oi;

        this.requires(elevator);
        this.requires(drive);
        this.requires(wrist);
        this.requires(intake);
        this.requires(leaner);
        this.requires(climber);
        this.requires(climbDeploy);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        int tick = elevator.getCurrentTick();
        maxTick = tick;
        minTick = tick;
        elevator.uncalibrate();
    }

    /**
     * By getting the delta Tick and the delta Height in Inches, we can get Tick per Inches
     */
    @Override
    public void execute() {
        // Stuff we do on every execute, since we want to read a lot of information
        int tick = elevator.getCurrentTick();
        if (tick > maxTick) {
            maxTick = tick;
        }

        if (tick < minTick) {
            minTick = tick;
        }

        // Directly control the elevator with a joystick

        elevator.insanelyDangerousSetPower(oi.operatorGamepad.getRightStickY());
    }

    @Override
    public void end() {
        double minHeightInInches = elevator.getMinHeightInInches();
        double maxHeightInInches = elevator.getMaxHeightInInches();
        
        log.info("Largest tick: " + maxTick + ", Smallest tick: " + minTick);
        
        double ticksPerInch = ((maxTick - minTick) / (maxHeightInInches - minHeightInInches));
        
        log.info("Calculated TPI: " + ticksPerInch);
        
        // some code here to set the ticks per inch on the subsystem
        elevator.setTickPerInch(ticksPerInch);
        elevator.calibrateAt(minTick);
    }
}
