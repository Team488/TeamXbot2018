package competition.subsystems.shift.commands;

import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.command.BaseCommand;

public class ShiftLowCommand extends BaseCommand {
	ShiftSubsystem shiftSubsystem;

	public ShiftLowCommand(ShiftSubsystem subsystem) {
		shiftSubsystem = subsystem;
		this.requires(shiftSubsystem);
	}

	@Override
	public void initialize() {
		log.info("shifting low");
		shiftSubsystem.setGear(Gear.LOW_GEAR);
	}

	@Override
	public void execute() {

	}

	public boolean isFinished() {
		return true;
	}
}
