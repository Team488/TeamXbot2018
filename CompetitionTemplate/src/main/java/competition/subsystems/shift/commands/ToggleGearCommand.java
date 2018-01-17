package competition.subsystems.shift.commands;

import com.google.inject.Inject;

import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import xbot.common.command.BaseCommand;

public class ToggleGearCommand extends BaseCommand {

	private ShiftSubsystem shiftSubsystem;
	private Gear gear;

	@Inject
	public ToggleGearCommand(ShiftSubsystem shiftSubsystem) {
		this.shiftSubsystem = shiftSubsystem;
		this.requires(shiftSubsystem);
	}

	@Override
	public void initialize() {
		log.info("initializing");
		if (gear == Gear.LOW_GEAR) {
			log.info("shifting high");
			gear = Gear.HIGH_GEAR;
		} else {
			log.info("shifting low");
			gear = Gear.LOW_GEAR;
		}

		shiftSubsystem.setGear(gear);
	}

	@Override
	public void execute() {

	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
