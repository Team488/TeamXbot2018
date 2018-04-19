package competition.subsystems.gripperintake.commands;

import com.google.inject.Inject;

import competition.subsystems.gripperintake.GripperIntakeSubsystem;

import xbot.common.command.BaseCommand;

public class GripperEjectCommand extends BaseCommand {

    GripperIntakeSubsystem intake;
    private boolean highPower = false;

    @Inject
    public GripperEjectCommand(GripperIntakeSubsystem intake) {
        this.requires(intake);
        this.intake = intake;
    }

    @Override
    public void initialize() {
        log.info("Initializing");
    }
    
    public void setIsHighPower(boolean isHighPower) {
        this.highPower = isHighPower;
    }

    @Override
    public void execute() {
        if (highPower) {
            intake.ejectHighPower();
        }
        else {
            intake.eject();
        }
    }

}
