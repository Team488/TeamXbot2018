package competition.subsystems.drive.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.drive.DriveSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.math.PIDManager;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.control_logic.HeadingAssistModule;
import xbot.common.subsystems.drive.control_logic.HeadingModule;

public class AssistedTankDriveCommand extends BaseCommand {

    final DriveSubsystem drive;
    final OperatorInterface oi;
    final HeadingAssistModule ham;
    
    @Inject
    public AssistedTankDriveCommand(CommonLibFactory clf, PIDFactory pf, DriveSubsystem drive, OperatorInterface oi) {
        this.drive = drive;
        this.oi = oi;
        this.requires(drive);
        
        PIDManager headingPid = pf.createPIDManager("DriveHeading", 4, 0, 0);
        PIDManager decayPid = pf.createPIDManager("DriveDecay", 0, 0, 1);
        
        HeadingModule holdHeading = clf.createHeadingModule(headingPid);
        HeadingModule decayHeading = clf.createHeadingModule(decayPid);
        ham = clf.createHeadingAssistModule(holdHeading, decayHeading);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
    }

    @Override
    public void execute() {
        double left = oi.driverGamepad.getLeftVector().y; 
        double right = oi.driverGamepad.getRightVector().y;
        
        double yTranslate = (left+right) / 2;
        double turn = ham.calculateHeadingPower((right-left)/2);
        
        drive.drive(new XYPair(0, yTranslate), turn);
    }

}
