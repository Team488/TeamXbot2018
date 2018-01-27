package competition.subsystems.elevator.commands;

import com.google.inject.Singleton;

import xbot.common.command.BaseSubsystem;

@Singleton
public class StopCommand extends BaseSubsystem {

    double power;

    public void execute() {

        power = 0;

    }
}
