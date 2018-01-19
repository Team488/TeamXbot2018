package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;

public class TankDriveWithJoysticksCommand extends BaseCommand {

    final DriveSubsystem driveSubsystem;
    final OperatorInterface oi;

    @Inject
    public TankDriveWithJoysticksCommand(OperatorInterface oi, DriveSubsystem driveSubsystem) {
        this.oi = oi;
        this.driveSubsystem = driveSubsystem;
        this.requires(this.driveSubsystem);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
    	
    	//log.info("LV: " + oi.gamepad.getLeftVector().y + ", RV: " + oi.gamepad.getRightVector().y );
        driveSubsystem.drive(
        		oi.gamepad.getLeftVector().y, 
        		oi.gamepad.getRightVector().y);
    }

}
