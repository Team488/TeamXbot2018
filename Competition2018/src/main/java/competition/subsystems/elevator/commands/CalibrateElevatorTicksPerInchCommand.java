package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorCommandMap;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class CalibrateElevatorTicksPerInchCommand extends BaseCommand {

    double maxTick;
    double minTick;
    ElevatorSubsystem elevator;
    OperatorInterface oi;

    @Inject
    public CalibrateElevatorTicksPerInchCommand(ElevatorSubsystem elevator,OperatorInterface oi) {
        this.elevator = elevator;
        this.oi=oi;
        this.requires(elevator);
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

        elevator.setPower(oi.gamepad.getRightVector().y);
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
