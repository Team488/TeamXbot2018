package competition.subsystems.shift.commands;

import com.google.inject.Inject;

import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.command.BaseCommand;

public class ShiftHighCommand extends BaseCommand {
	ShiftSubsystem shiftSubsystem;

	@Inject
	public ShiftHighCommand(ShiftSubsystem subsystem) {
		shiftSubsystem = subsystem;
		this.requires(shiftSubsystem);
	}

	@Override
	public void initialize() {
		log.info("shifting high");
		shiftSubsystem.setGear(Gear.HIGH_GEAR);
	}

	@Override
	public void execute() {

	}

	public boolean isFinished() {
		return true;
	}

}
