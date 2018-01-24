package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class CalibrateCommand extends BaseCommand {

    double maxTick;
    double minTick;
    ElevatorSubsystem elevator;
    OperatorInterface oi;

    @Inject
    public CalibrateCommand(ElevatorSubsystem elevator) {
        // TODO Auto-generated constructor stub
        this.elevator = elevator;
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        double tick = elevator.currentTick();
        maxTick = tick;
        minTick = tick;

    }

    /**
     * By getting the delta Tick and the delta Height in Inches, we can get Tick per Inches
     */
    @Override
    public void execute() {
        // Stuff we do on every execute, since we want to read a lot of information
        double tick = elevator.currentTick();

        if (tick > maxTick) {
            maxTick = tick;
        }

        if (tick < minTick) {
            minTick = tick;
        }
        
        // Directly control the elevator with a joystick
        elevator.setPower(0);
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
