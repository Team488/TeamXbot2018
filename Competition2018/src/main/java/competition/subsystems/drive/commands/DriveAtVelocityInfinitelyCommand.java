package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import xbot.common.command.BaseCommand;
import xbot.common.properties.XPropertyManager;
import competition.subsystems.drive.DriveSubsystem;

public class DriveAtVelocityInfinitelyCommand extends BaseCommand {
    private final DriveSubsystem drive;
    private double velocity;
    
    @Inject
    public DriveAtVelocityInfinitelyCommand(DriveSubsystem drive, XPropertyManager propManager) {
        this.drive = drive;
        requires(drive);
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
    
    @Override
    public void initialize() {
        log.info("Initializing with velocity " + velocity);
    }

    @Override
    public void execute() {
        drive.driveTankVelocity(velocity, velocity);
    }
    
    @Override
    public void end() {
        drive.drive(0, 0);
        super.end();
    }
}
