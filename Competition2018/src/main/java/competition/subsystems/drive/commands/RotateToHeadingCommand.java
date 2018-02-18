package competition.subsystems.drive.commands;

import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.subsystems.drive.control_logic.HeadingModule;
import com.google.inject.Inject;
import competition.subsystems.drive.DriveSubsystem;

public class RotateToHeadingCommand extends BaseCommand{

    double goal;
    HeadingModule headingModule;
    DriveSubsystem drive;
    
    @Inject
    public RotateToHeadingCommand(CommonLibFactory clf, PIDFactory pf,  DriveSubsystem drive) {
        this.drive = drive;
        headingModule = clf.createHeadingModule(drive.getRotateToHeadingPid());  
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        headingModule.reset();  
    }

    @Override
    public void execute() {
        double power = headingModule.calculateHeadingPower(goal);
        drive.drive(-power, power);        
    }
    
    public void setGoalHeading(double goal) {
        this.goal = goal;
    }
}
