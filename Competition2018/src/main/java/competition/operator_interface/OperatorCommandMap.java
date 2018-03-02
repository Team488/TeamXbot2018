package competition.operator_interface;

import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.commandgroups.EngageWinchAndLockPawlCommandGroup;
import competition.commandgroups.CollectCubeCommandGroup;
import competition.commandgroups.DynamicScoreOnSwitchCommandGroup;
import competition.commandgroups.DisengageWinchAndReleasePawlCommandGroup;
import competition.subsystems.autonomous.commands.ChangeAutoDelayCommand;
import competition.subsystems.autonomous.selection.SelectCrossLineCommand;
import competition.subsystems.autonomous.selection.SelectDoNothingCommand;
import competition.subsystems.autonomous.selection.SelectDynamicScoreOnScaleCommand;
import competition.subsystems.autonomous.selection.SelectDynamicScoreOnSwitchCommand;
import competition.subsystems.autonomous.selection.SetStartingSideCommand;
import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.DecendClimberCommand;
import competition.subsystems.climb.commands.EngagePawlCommand;
import competition.subsystems.climb.commands.ReleasePawlCommand;
import competition.subsystems.climberdeploy.commands.ExtendClimberArmCommand;
import competition.subsystems.climberdeploy.commands.RetractClimberArmCommand;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.DriveAtVelocityInfinitelyCommand;
import competition.subsystems.drive.commands.FieldOrientedTankDriveCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.CalibrateElevatorHereCommand;
import competition.subsystems.elevator.commands.CalibrateElevatorTicksPerInchCommand;
import competition.subsystems.elevator.commands.ControlElevatorViaJoystickCommand;
import competition.subsystems.elevator.commands.DisableElevatorCurrentLimitCommand;
import competition.subsystems.elevator.commands.ElevatorMaintainerCommand;
import competition.subsystems.elevator.commands.ElevatorUncalibrateCommand;
import competition.subsystems.elevator.commands.ElevatorVelocityCommand;
import competition.subsystems.elevator.commands.EnableElevatorCurrentLimitCommand;
import competition.subsystems.elevator.commands.ExperimentMotionMagicCommand;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.gripperintake.commands.GripperRotateClockwiseCommand;
import competition.subsystems.gripperintake.commands.GripperRotateCounterClockwiseCommand;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.commands.AcquireVisibleCubeCommand;
import competition.subsystems.offboard.commands.NavToTestGoalCommand;
import competition.subsystems.offboard.data.TargetCubeInfo;
import competition.subsystems.pose.PoseSubsystem;
import competition.subsystems.power_state_manager.commands.EnterLowBatteryModeCommand;
import competition.subsystems.power_state_manager.commands.LeaveLowBatteryModeCommand;
import competition.subsystems.shift.commands.ShiftHighCommand;
import competition.subsystems.shift.commands.ShiftLowCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import competition.subsystems.wrist.commands.WristCalibrateCommand;
import competition.subsystems.wrist.commands.WristMaintainerCommand;
import competition.subsystems.wrist.commands.WristUncalibrateCommand;
import competition.subsystems.zed_deploy.commands.ExtendRetractZedCommand;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.properties.ConfigurePropertiesCommand;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.PurePursuitCommand.PursuitMode;
import xbot.common.subsystems.pose.commands.ResetDistanceCommand;
import xbot.common.subsystems.pose.commands.SetRobotHeadingCommand;

@Singleton
public class OperatorCommandMap {
    // For mapping operator interface buttons to commands

    // Example for setting up a command to fire when a button is pressed:
    /*
     * @Inject public void setupMyCommands( OperatorInterface operatorInterface, MyCommand myCommand) {
     * operatorInterface.leftButtons.getifAvailable(1).whenPressed(myCommand); }
     */

    @Inject
    public void setupMiscCommands(ConfigurePropertiesCommand fastMode, ConfigurePropertiesCommand slowMode) {
        fastMode.setFastMode(true);
        slowMode.setFastMode(false);

        fastMode.includeOnSmartDashboard("Properties FastMode");
        slowMode.includeOnSmartDashboard("Properties SlowMode");
    }
    
    @Inject
    public void setupAutoCommands(
            OperatorInterface oi,
            ChangeAutoDelayCommand addAutoDelay,
            ChangeAutoDelayCommand subtractAutoDelay,
            SelectDynamicScoreOnScaleCommand selectScale,
            SelectDynamicScoreOnSwitchCommand selectSwitch,
            SelectCrossLineCommand crossLine,
            SelectDoNothingCommand doNothing,
            SetStartingSideCommand setLeft,
            SetStartingSideCommand setRight) {
        
        addAutoDelay.setDelayChangeAmount(1);
        subtractAutoDelay.setDelayChangeAmount(-1);
        
        setLeft.setRightSide(false);
        setRight.setRightSide(true);
        
        oi.programmerGamepad.getPovIfAvailable(0).whenPressed(addAutoDelay);
        oi.programmerGamepad.getPovIfAvailable(180).whenPressed(subtractAutoDelay);
        oi.programmerGamepad.getPovIfAvailable(90).whenPressed(setRight);
        oi.programmerGamepad.getPovIfAvailable(270).whenPressed(setLeft);
        
        oi.programmerGamepad.getifAvailable(1).whenPressed(selectSwitch);
        oi.programmerGamepad.getifAvailable(2).whenPressed(selectScale);        
        oi.programmerGamepad.getifAvailable(3).whenPressed(crossLine);
        oi.programmerGamepad.getifAvailable(4).whenPressed(doNothing);
    }

    @Inject
    public void setupDriveCommands(OperatorInterface oi, ResetDistanceCommand resetDistance,
            SetRobotHeadingCommand setHeading, DynamicScoreOnSwitchCommandGroup dynamicScore,
            FieldOrientedTankDriveCommand fieldTank, ArcadeDriveWithJoysticksCommand arcade) {

        resetDistance.includeOnSmartDashboard();
        setHeading.setHeadingToApply(90);
        setHeading.includeOnSmartDashboard();

        dynamicScore.includeOnSmartDashboard();
        
        oi.driverGamepad.getPovIfAvailable(0).whenPressed(fieldTank);
        oi.driverGamepad.getPovIfAvailable(180).whenPressed(arcade);
    }

    @Inject
    public void setupShiftGearCommand(OperatorInterface oi, ShiftHighCommand shiftHigh, ShiftLowCommand shiftLow) {

        oi.driverGamepad.getifAvailable(5).whenPressed(shiftLow);
        oi.driverGamepad.getifAvailable(6).whenPressed(shiftHigh);
    }

    @Inject
    public void setupGripperCommands(OperatorInterface oi, GripperRotateClockwiseCommand clockwise,
            GripperRotateCounterClockwiseCommand counterClockwise, GripperEjectCommand eject,
            GripperIntakeCommand intake) {
    }

    @Inject
    public void setupElevatorCommands(OperatorInterface oi, CalibrateElevatorTicksPerInchCommand calibrateElevatorTicks,
            ElevatorUncalibrateCommand uncalibrate, ElevatorMaintainerCommand maintainer,
            SetElevatorTargetHeightCommand targetScaleHighHeight, SetElevatorTargetHeightCommand targetScaleMidHeight,
            SetElevatorTargetHeightCommand targetSwitchDropHeight, SetElevatorTargetHeightCommand targetPickUpHeight,
            CalibrateElevatorHereCommand calibrateHere, EnableElevatorCurrentLimitCommand enableCurrentLimit,
            DisableElevatorCurrentLimitCommand disableCurrentLimit, ExperimentMotionMagicCommand mm,
            ControlElevatorViaJoystickCommand joysticks, ElevatorVelocityCommand velocity,
            ElevatorSubsystem elevatorSubsystem) {
        oi.operatorGamepad.getifAvailable(5).whileHeld(calibrateElevatorTicks);
        oi.operatorGamepad.getifAvailable(6).whenPressed(joysticks);
        oi.operatorGamepad.getifAvailable(7).whenPressed(velocity);
        oi.operatorGamepad.getifAvailable(8).whenPressed(velocity);

        targetPickUpHeight.setGoalHeight(elevatorSubsystem.getTargetPickUpHeight());
        targetSwitchDropHeight.setGoalHeight(elevatorSubsystem.getTargetSwitchDropHeight());
        targetScaleMidHeight.setGoalHeight(elevatorSubsystem.getTargetScaleMidHeight());
        targetScaleHighHeight.setGoalHeight(elevatorSubsystem.getTargetScaleHighHeight());

        oi.operatorGamepad.getifAvailable(1).whenPressed(targetPickUpHeight);
        oi.operatorGamepad.getifAvailable(2).whenPressed(targetSwitchDropHeight);
        oi.operatorGamepad.getifAvailable(3).whenPressed(targetScaleMidHeight);
        oi.operatorGamepad.getifAvailable(4).whenPressed(targetScaleHighHeight);

        oi.operatorGamepad.getifAvailable(10).whenPressed(calibrateHere);

        uncalibrate.includeOnSmartDashboard();

        enableCurrentLimit.includeOnSmartDashboard();
        disableCurrentLimit.includeOnSmartDashboard();
        mm.includeOnSmartDashboard();
    }

    @Inject
    public void setupClimberCommands(OperatorInterface oi, ExtendClimberArmCommand extendArm, RetractClimberArmCommand retractArm, ReleasePawlCommand releasePawl,
            EngagePawlCommand engageWinch, DisengageWinchAndReleasePawlCommandGroup decend, EngageWinchAndLockPawlCommandGroup ascend) {
        oi.driverGamepad.getifAvailable(1).whileHeld(extendArm); // a
        oi.driverGamepad.getifAvailable(2).whileHeld(retractArm); // b
        oi.driverGamepad.getifAvailable(3).whenPressed(engageWinch); // x
        oi.driverGamepad.getifAvailable(4).whenPressed(releasePawl); // y
        oi.driverGamepad.getAnalogIfAvailable(oi.raiseClimber).whileHeld(ascend); //axis 3
        oi.driverGamepad.getAnalogIfAvailable(oi.lowerClimber).whileHeld(decend); //axis 2
    }

    @Inject
    public void setupCollectCubeCommandGroup(OperatorInterface oi, CollectCubeCommandGroup collectCube) {
        // oi.operatorGamepad.getifAvailable(7).whileHeld(collectCube);
    }

    @Inject
    public void setupVisionCommands(
                OperatorInterface oi,
                AcquireVisibleCubeCommand acquireCube,
                NavToTestGoalCommand testNav,
                DriveAtVelocityInfinitelyCommand driveAtVelLow,
                DriveAtVelocityInfinitelyCommand driveAtVelHigh,
                ConfigurablePurePursuitCommand driveToLocalCubeCommand,
                OffboardInterfaceSubsystem offboardSubsystem,
                PoseSubsystem poseSubsystem,
                ExtendRetractZedCommand extendZed,
                ExtendRetractZedCommand retractZed) {
        acquireCube.includeOnSmartDashboard();
        testNav.includeOnSmartDashboard();

        driveAtVelLow.setVelocity(20);
        driveAtVelLow.includeOnSmartDashboard("Test drive at velocity (low)");

        driveAtVelHigh.setVelocity(50);
        driveAtVelHigh.includeOnSmartDashboard("Test drive at velocity (high)");

        driveToLocalCubeCommand.setMode(PursuitMode.Relative);
        driveToLocalCubeCommand.setPointSupplier(() -> {
            TargetCubeInfo targetCube = offboardSubsystem.getTargetCube();
            if (targetCube == null) {
                return null;
            }
            double headingDelta = Math.toDegrees(Math.atan2(targetCube.xInches, targetCube.yInches));

            FieldPose targetPose = new FieldPose(new XYPair(targetCube.xInches, targetCube.yInches),
                    new ContiguousHeading(90 - headingDelta));
            return Arrays.asList(targetPose);
        });

        driveToLocalCubeCommand.includeOnSmartDashboard("Drive to local cube");
        oi.driverGamepad.getifAvailable(7).whilePressedNoRestart(driveToLocalCubeCommand);
        oi.driverGamepad.getifAvailable(8).whilePressedNoRestart(driveToLocalCubeCommand);

        extendZed.setIsExtended(true);
        extendZed.includeOnSmartDashboard("Extend ZED");
        
        retractZed.setIsExtended(false);
        retractZed.includeOnSmartDashboard("Retract ZED");
    }

    @Inject
    public void setupWristCommands(OperatorInterface oi, WristCalibrateCommand calibrate,
            WristUncalibrateCommand loseCalibration, WristMaintainerCommand maintain, SetWristAngleCommand low,
            SetWristAngleCommand medium, SetWristAngleCommand high) {
        oi.operatorGamepad.getifAvailable(9).whenPressed(calibrate);
        loseCalibration.includeOnSmartDashboard();

        low.setGoalAngle(10);
        medium.setGoalAngle(60);
        high.setGoalAngle(90);

        oi.operatorGamepad.getPovIfAvailable(270).whenPressed(maintain);
        oi.operatorGamepad.getPovIfAvailable(0).whenPressed(high);
        oi.operatorGamepad.getPovIfAvailable(90).whenPressed(medium);
        oi.operatorGamepad.getPovIfAvailable(180).whenPressed(low);
    }

    @Inject
    public void setupLowBatteryCommands(
            OperatorInterface oi,
            EnterLowBatteryModeCommand enter,
            LeaveLowBatteryModeCommand leave) {
        oi.driverGamepad.getifAvailable(9).whenPressed(enter);
        oi.driverGamepad.getifAvailable(10).whenPressed(leave);
        
        enter.includeOnSmartDashboard();
        leave.includeOnSmartDashboard();
    }
}
