package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorCommandMap;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.climb.ClimbSubsystem;
import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import competition.subsystems.lean.LeanSubsystem;
import xbot.common.command.BaseCommand;

public class CalibrateElevatorTicksPerInchCommand extends BaseCommand {

    double maxTick;
    double minTick;
    ElevatorSubsystem elevator;
    OperatorInterface oi;

    /**
     * This command needs to require all possible subsystems - while calibrating the elevator we DO NOT WANT
     * ANYTHING ELSE TO MOVE.
     */
    @Inject
    public CalibrateElevatorTicksPerInchCommand(
            ElevatorSubsystem elevator,
            DriveSubsystem drive,
            GripperDeploySubsystem wrist,
            GripperIntakeSubsystem intake,
            LeanSubsystem leaner,
            ClimbSubsystem climber,
            ClimberDeploySubsystem climbDeploy,
            OperatorInterface oi) {
        this.elevator = elevator;
        this.oi=oi;
        
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
        double tick = elevator.getCurrentTick();
        maxTick = tick;
        minTick = tick;

    }

    /**
     * By getting the delta Tick and the delta Height in Inches, we can get Tick per Inches
     */
    @Override
    public void execute() {
        // Stuff we do on every execute, since we want to read a lot of information
        double tick = elevator.getCurrentTick();
        if (tick > maxTick) {
            maxTick = tick;
        }

        if (tick < minTick) {
            minTick = tick;
        }
        
        // Directly control the elevator with a joystick

        elevator.setPower(oi.operatorGamepad.getRightVector().y);
    }
    
    @Override
    public void end() {
        double minHeightInInches = elevator.minHeightInInches();
        double maxHeightInInches = elevator.maxHeightInInches();
        double ticksPerInch = ((maxTick - minTick) / (maxHeightInInches - minHeightInInches));
        // some code here to set the ticks per inch on the subsystem
        elevator.setTickPerInch(ticksPerInch);
    }
}
