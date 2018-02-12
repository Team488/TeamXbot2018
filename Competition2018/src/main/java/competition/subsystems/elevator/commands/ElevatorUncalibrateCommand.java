package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

public class ElevatorUncalibrateCommand extends BaseCommand {

    ElevatorSubsystem elevator;
    
    @Inject
    public ElevatorUncalibrateCommand(ElevatorSubsystem elevator) {
        this.elevator = elevator;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        elevator.uncalibrate();
    }

    @Override
    public void execute() {
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }

}
