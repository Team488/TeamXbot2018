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
    double power;
    double heading;
    final CommonLibFactory clf;
    final PIDManager pid;
    HeadingModule headingModule;
    DriveSubsystem drive;
    
    @Inject
    public RotateToHeadingCommand(CommonLibFactory clf, PIDFactory pf,  DriveSubsystem drive) {
        this.clf = clf;
        this.drive = drive;
        pid = pf.createPIDManager("Goal Heading", 1, 0, 0);
        pid.setErrorThreshold(0.1);
        headingModule = clf.createHeadingModule(pid); 
        
    }

    @Override
    public void initialize() {
        log.info("Initializing");
        headingModule.reset();
        
    }

    @Override
    public void execute() {
        power = headingModule.calculateHeadingPower(goal);
        drive.drive(-power, power);        
    }
    
    public double getRotatePower() {
        return power;
    }
    
    public void setGoalHeading(double goal) {
        this.goal = goal;
    }
}
