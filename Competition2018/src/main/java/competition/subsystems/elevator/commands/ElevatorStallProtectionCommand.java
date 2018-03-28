package competition.subsystems.elevator.commands;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.sensors.TalonCurrentMonitor;
import xbot.common.properties.XPropertyManager;

public class ElevatorStallProtectionCommand extends BaseCommand {
    
    final ElevatorSubsystem elevator;
    TalonCurrentMonitor currentMonitor;
    
    public ElevatorStallProtectionCommand(XPropertyManager propMan, ElevatorSubsystem elevator) {
        this.elevator = elevator;
        this.currentMonitor = new TalonCurrentMonitor(elevator.master);
        this.requires(elevator);
    }

    @Override
    public void initialize() {

        
    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
      //  return
    }

    @Override
    public void end() {
        elevator.stop();
    }
    
}
