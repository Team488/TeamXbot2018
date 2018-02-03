package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

@Singleton
public class LowerCommand extends BaseCommand {

    ElevatorSubsystem lower;

    @Inject
    public LowerCommand(ElevatorSubsystem lower) {
        this.lower = lower;
    }
    
    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        lower.lower();

    }

}
