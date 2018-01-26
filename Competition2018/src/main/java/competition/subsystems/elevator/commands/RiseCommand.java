package competition.subsystems.elevator.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;

@Singleton
public class RiseCommand extends BaseCommand {

    ElevatorSubsystem rise;

    @Inject
    public RiseCommand(ElevatorSubsystem rise) {
        this.rise = rise;
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute() {
        rise.rise();
    }

}
