package competition.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.ElectricalContract2018;
import competition.subsystems.climberdeploy.ClimberDeploySubsystem;
import competition.subsystems.climberdeploy.commands.StopClimberArmCommand;
import competition.subsystems.drive.DriveSubsystem;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.ControlElevatorViaJoystickCommand;
import competition.subsystems.gripperintake.GripperIntakeSubsystem;
import competition.subsystems.lean.LeanSubsystem;
import competition.subsystems.lean.commands.StopLeaningCommand;
import competition.subsystems.shift.ShiftSubsystem;
import competition.subsystems.shift.commands.ShiftLowCommand;
import competition.subsystems.wrist.WristSubsystem;
import competition.subsystems.wrist.commands.WristControlViaJoysticksCommand;
import competition.subsystems.wrist.commands.WristStopCommand;

@Singleton
public class SubsystemDefaultCommandMap {
    // For setting the default commands on subsystems

    @Inject
    public void setupDriveSubsystem(DriveSubsystem driveSubsystem, ArcadeDriveWithJoysticksCommand command) {
        driveSubsystem.setDefaultCommand(command);
    }
    
    @Inject
    public void setupElevatorSubsystem(
            ElectricalContract2018 contract,
            ElevatorSubsystem elevator,
            ControlElevatorViaJoystickCommand controlWithJoystick) {
        if (contract.elevatorReady()) {
            elevator.setDefaultCommand(controlWithJoystick);
        }
    }

    @Inject
    public void setupClimberDeploySubsystem(
            ElectricalContract2018 contract, 
            ClimberDeploySubsystem climberdeploySubsystem,
            StopClimberArmCommand command) {
        if (contract.climbDeployReady()) {
            climberdeploySubsystem.setDefaultCommand(command);
        }
    }

    @Inject
    public void setupWristSubsystem(
            ElectricalContract2018 contract,
            WristSubsystem wristSubsystem,
            WristControlViaJoysticksCommand command) {
        if (contract.wristReady()) {
            wristSubsystem.setDefaultCommand(command); 
        }
    }

    @Inject
    public void setupLeanSubsystem(
            ElectricalContract2018 contract,
            LeanSubsystem leanSubsystem, 
            StopLeaningCommand command) {
        if (contract.climbLeanReady()) {
            leanSubsystem.setDefaultCommand(command);
        }
    }

    @Inject
    public void setupGripperSubsystem(
            ElectricalContract2018 contract,
            GripperIntakeSubsystem gripperSubsystem, 
            WristStopCommand command) {
        if (contract.collectorReady()) {
            gripperSubsystem.setDefaultCommand(command);
        }
    }
    
    @Inject
    public void setupShiftSubsytem(
            ShiftSubsystem shiftSubsystem, 
            ShiftLowCommand command) {
        shiftSubsystem.setDefaultCommand(command);
    }
}
