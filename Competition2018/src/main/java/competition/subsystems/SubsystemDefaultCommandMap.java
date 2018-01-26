package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.climberdeploy.commands.StopClimberArmCommand;
import competition.subsystems.gripperdeploy.GripperDeploySubsystem;
import competition.subsystems.gripperdeploy.commands.GripperStopDeployCommand;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.StopLeaningCommand;
import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.commands.ShiftLowCommand;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, TankDriveWithJoysticksCommand command) {
        driveSubsystem.setDefaultCommand(command);
    }
    
    @Inject
    public void setupClimberDeploySubsystem(ClimberDeploySubsystem climberdeploySubsystem, StopClimberArmCommand command) {
    	climberdeploySubsystem.setDefaultCommand(command);
    }
    
    @Inject 
    public void setupGripperDeploySubsystem(GripperDeploySubsystem gripperdeploySubsystem, GripperStopDeployCommand command) {
    	gripperdeploySubsystem.setDefaultCommand(command);
    }
    
    @Inject
    public void setupLeanSubsystem(LeanSubsystem leanSubsystem, StopLeaningCommand command) {
    	leanSubsystem.setDefaultCommand(command);
    }
    
    @Inject
    public void setupShiftSubsytem(ShiftSubsystem shiftSubsystem, ShiftLowCommand command) {
    	shiftSubsystem.setDefaultCommand(command);
    }
}
