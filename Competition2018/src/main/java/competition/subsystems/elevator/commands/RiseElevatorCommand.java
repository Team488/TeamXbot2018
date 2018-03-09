package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

@Singleton
public class RiseElevatorCommand extends BaseCommand {

    ElevatorSubsystem elevator;

    @Inject
    public RiseElevatorCommand(ElevatorSubsystem elevator) {
        this.elevator = elevator;
        this.requires(elevator);
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        elevator.rise();
    }
}
