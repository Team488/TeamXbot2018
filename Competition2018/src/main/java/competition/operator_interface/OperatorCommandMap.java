package competition.operator_interface;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.commandgroups.CollectCubeCommandGroup;
import competition.commandgroups.ScoreOnSwitchCommandGroup;
import competition.subsystems.autonomous.AutonomousCommandSupplier.AutonomousMetaprogram;
import competition.subsystems.autonomous.AutonomousPathSupplier.StartingLocations;
import competition.subsystems.autonomous.commands.ChangeAutoDelayCommand;
import competition.subsystems.autonomous.selection.SelectAutonomousCommand;
import competition.subsystems.autonomous.selection.SetStartingSideCommand;
import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.AscendLowPowerCommand;
import competition.subsystems.drive.commands.AbsolutePurePursuit2018Command;
import competition.subsystems.drive.commands.ArcadeDriveWithJoysticksCommand;
import competition.subsystems.drive.commands.DriveAtVelocityInfinitelyCommand;
import competition.subsystems.drive.commands.FieldOrientedTankDriveCommand;
import competition.subsystems.drive.commands.TotalRobotPoint;
import competition.subsystems.drive.commands.VelocityArcadeDriveCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.CalibrateElevatorHereCommand;
import competition.subsystems.elevator.commands.CalibrateElevatorTicksPerInchCommand;
import competition.subsystems.elevator.commands.ControlElevatorViaJoystickCommand;
import competition.subsystems.elevator.commands.DisableElevatorCurrentLimitCommand;
import competition.subsystems.elevator.commands.ElevatorDangerousOverrideCommand;
import competition.subsystems.elevator.commands.ElevatorMaintainerCommand;
import competition.subsystems.elevator.commands.ElevatorUncalibrateCommand;
import competition.subsystems.elevator.commands.ElevatorVelocityCommand;
import competition.subsystems.elevator.commands.EnableElevatorCurrentLimitCommand;
import competition.subsystems.elevator.commands.ExperimentMotionMagicCommand;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperDropCubeCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.offboard.commands.AcquireVisibleCubeCommand;
import competition.subsystems.offboard.commands.IdentifyAndPurePursuitToVisibleCubeCommand;
import competition.subsystems.offboard.commands.IdentifyTargetCubeCommand.TimeoutPreset;
import competition.subsystems.offboard.commands.NavToTestGoalCommand;
import competition.subsystems.power_state_manager.commands.EnterLowBatteryModeCommand;
import competition.subsystems.power_state_manager.commands.LeaveLowBatteryModeCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.shift.commands.ShiftHighCommand;
import competition.subsystems.shift.commands.ShiftLowCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import competition.subsystems.wrist.commands.WristCalibrateCommand;
import competition.subsystems.wrist.commands.WristDangerousOverrideCommand;
import competition.subsystems.wrist.commands.WristMaintainerCommand;
import competition.subsystems.wrist.commands.WristUncalibrateCommand;
import competition.subsystems.zed_deploy.commands.ExtendRetractZedCommand;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.properties.ConfigurePropertiesCommand;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.drive.RabbitPoint.PointDriveStyle;
import xbot.common.subsystems.drive.RabbitPoint.PointTerminatingType;
import xbot.common.subsystems.drive.RabbitPoint.PointType;
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
    public void setupAutoCommands(OperatorInterface oi, ChangeAutoDelayCommand addAutoDelay,
            ChangeAutoDelayCommand subtractAutoDelay, SelectAutonomousCommand selectScale, SelectAutonomousCommand selectDoubleScale,
            SelectAutonomousCommand selectSwitch, SelectAutonomousCommand crossLine,
            SelectAutonomousCommand doNothing, SetStartingSideCommand setLeft, SetStartingSideCommand setRight,
            SetStartingSideCommand setMiddle, SelectAutonomousCommand selectDoubleSwitch) {

        selectScale.setMetaprogram(AutonomousMetaprogram.Scale);
        selectDoubleScale.setMetaprogram(AutonomousMetaprogram.DoubleScale);
        selectSwitch.setMetaprogram(AutonomousMetaprogram.Switch);
        selectDoubleSwitch.setMetaprogram(AutonomousMetaprogram.DoubleSwitch);
        crossLine.setMetaprogram(AutonomousMetaprogram.CrossLine);
        doNothing.setMetaprogram(AutonomousMetaprogram.DoNothing);

        addAutoDelay.setDelayChangeAmount(1);
        subtractAutoDelay.setDelayChangeAmount(-1);

        setLeft.setStartingLocation(StartingLocations.Left);
        setRight.setStartingLocation(StartingLocations.Right);
        setMiddle.setStartingLocation(StartingLocations.Middle);

        oi.programmerGamepad.getPovIfAvailable(0).whenPressed(addAutoDelay);
        oi.programmerGamepad.getPovIfAvailable(180).whenPressed(subtractAutoDelay);
        oi.programmerGamepad.getPovIfAvailable(90).whenPressed(setRight);
        oi.programmerGamepad.getPovIfAvailable(270).whenPressed(setLeft);
        oi.programmerGamepad.getifAvailable(5).whenPressed(setMiddle);

        oi.programmerGamepad.getifAvailable(1).whenPressed(selectSwitch);
        oi.programmerGamepad.getifAvailable(2).whenPressed(selectScale);
        oi.programmerGamepad.getifAvailable(3).whenPressed(crossLine);
        oi.programmerGamepad.getifAvailable(4).whenPressed(doNothing);
        oi.programmerGamepad.getifAvailable(6).whenPressed(selectDoubleScale);
        oi.programmerGamepad.getifAvailable(8).whenPressed(selectDoubleSwitch);
    }

    @Inject
    public void setupDriveCommands(OperatorInterface oi, ResetDistanceCommand resetDistance,
            SetRobotHeadingCommand setHeading, ScoreOnSwitchCommandGroup dynamicScore,
            FieldOrientedTankDriveCommand fieldTank, ArcadeDriveWithJoysticksCommand arcade,
            VelocityArcadeDriveCommand velocity, ConfigurablePurePursuitCommand square,
            AbsolutePurePursuit2018Command pretendMulticube) {

        resetDistance.includeOnSmartDashboard();
        setHeading.setHeadingToApply(90);
        setHeading.includeOnSmartDashboard();

        dynamicScore.includeOnSmartDashboard();

        oi.driverGamepad.getPovIfAvailable(0).whenPressed(velocity);
        oi.driverGamepad.getPovIfAvailable(180).whenPressed(arcade);

        pretendMulticube.addPoint(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0, -3 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        pretendMulticube
                .addPoint(new TotalRobotPoint(
                        new RabbitPoint(new FieldPose(new XYPair(0, 0), new ContiguousHeading(225)),
                                PointType.HeadingOnly, PointTerminatingType.Continue, PointDriveStyle.Macro),
                        Gear.LOW_GEAR, 80));

        pretendMulticube.addPoint(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(-3 * 12, -6 * 12), new ContiguousHeading(225)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Micro),
                Gear.LOW_GEAR, 40));

        pretendMulticube.addPoint(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, -6 * 12), new ContiguousHeading(180)),
                        PointType.PositionAndHeading, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        pretendMulticube.addPoint(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(90)),
                        PointType.HeadingOnly, PointTerminatingType.Continue, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        pretendMulticube.addPoint(new TotalRobotPoint(
                new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(90)),
                        PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro),
                Gear.LOW_GEAR, 80));

        pretendMulticube.includeOnSmartDashboard("A Pretent Multicube");

        square.addPoint(new RabbitPoint(new FieldPose(new XYPair(0 * 12, 6 * 12), new ContiguousHeading(90)),
                PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro));
        square.addPoint(new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(0)),
                PointType.HeadingOnly, PointTerminatingType.Stop, PointDriveStyle.Macro));

        square.addPoint(new RabbitPoint(new FieldPose(new XYPair(6 * 12, 6 * 12), new ContiguousHeading(0)),
                PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro));
        square.addPoint(new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(-90)),
                PointType.HeadingOnly, PointTerminatingType.Stop, PointDriveStyle.Macro));

        square.addPoint(new RabbitPoint(new FieldPose(new XYPair(6 * 12, 0 * 12), new ContiguousHeading(-90)),
                PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro));
        square.addPoint(new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(180)),
                PointType.HeadingOnly, PointTerminatingType.Stop, PointDriveStyle.Macro));

        square.addPoint(new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(180)),
                PointType.PositionAndHeading, PointTerminatingType.Stop, PointDriveStyle.Macro));
        square.addPoint(new RabbitPoint(new FieldPose(new XYPair(0 * 12, 0 * 12), new ContiguousHeading(90)),
                PointType.HeadingOnly, PointTerminatingType.Stop, PointDriveStyle.Macro));

        square.includeOnSmartDashboard("A Square Dancing Robot");
    }

    @Inject
    public void setupShiftGearCommand(OperatorInterface oi, ShiftHighCommand shiftHigh, ShiftLowCommand shiftLow) {

        oi.driverGamepad.getifAvailable(5).whenPressed(shiftLow);
        oi.driverGamepad.getifAvailable(6).whenPressed(shiftHigh);
    }

    @Inject
    public void setupGripperCommands(OperatorInterface oi, GripperEjectCommand eject, GripperIntakeCommand intake,
            GripperDropCubeCommand dropCube) {

        oi.operatorGamepad.getifAvailable(8).whileHeld(dropCube);
        oi.operatorGamepad.getifAvailable(3).whileHeld(dropCube);

        oi.driverGamepad.getAnalogIfAvailable(oi.driverLeftTrigger).whileHeld(eject);
        oi.driverGamepad.getAnalogIfAvailable(oi.driverRightTrigger).whileHeld(dropCube);
    }

    @Inject
    public void setupElevatorCommands(OperatorInterface oi, CalibrateElevatorTicksPerInchCommand calibrateElevatorTicks,
            ElevatorUncalibrateCommand uncalibrate, ElevatorMaintainerCommand maintainer,
            SetElevatorTargetHeightCommand targetScaleHighHeight, SetElevatorTargetHeightCommand targetScaleMidHeight,
            SetElevatorTargetHeightCommand targetSwitchDropHeight, SetElevatorTargetHeightCommand targetPickUpHeight,
            SetElevatorTargetHeightCommand targetPortalHeight, CalibrateElevatorHereCommand calibrateHere,
            EnableElevatorCurrentLimitCommand enableCurrentLimit,
            DisableElevatorCurrentLimitCommand disableCurrentLimit, ExperimentMotionMagicCommand mm,
            ControlElevatorViaJoystickCommand joysticks, ElevatorVelocityCommand velocity,
            ElevatorDangerousOverrideCommand dangerousOverride, ElevatorSubsystem elevatorSubsystem) {

        // calibrateElevatorTicks.includeOnSmartDashboard();

        oi.operatorGamepad.getifAvailable(6).whileHeld(dangerousOverride);
        oi.operatorGamepad.getifAvailable(7).whenPressed(velocity);

        targetPickUpHeight.setGoalHeight(elevatorSubsystem.getTargetPickUpHeight());
        targetSwitchDropHeight.setGoalHeight(elevatorSubsystem.getTargetSwitchDropHeight());
        targetScaleMidHeight.setGoalHeight(elevatorSubsystem.getTargetScaleMidHeight());
        targetScaleHighHeight.setGoalHeight(elevatorSubsystem.getTargetScaleHighHeight());
        targetPortalHeight.setGoalHeight(elevatorSubsystem.getTargetExchangeZonePickUpHeight());

        oi.operatorGamepad.getifAvailable(1).whenPressed(targetPickUpHeight);
        oi.operatorGamepad.getifAvailable(2).whenPressed(targetPortalHeight);
        //oi.operatorGamepad.getifAvailable(3).whenPressed(targetSwitchDropHeight);
        oi.operatorGamepad.getifAvailable(4).whenPressed(targetScaleMidHeight);

        oi.operatorGamepad.getifAvailable(10).whenPressed(calibrateHere);

        uncalibrate.includeOnSmartDashboard();

        enableCurrentLimit.includeOnSmartDashboard();
        disableCurrentLimit.includeOnSmartDashboard();
        mm.includeOnSmartDashboard();
    }

    @Inject
    public void setupClimberCommands(OperatorInterface oi, AscendClimberCommand ascend,
            AscendLowPowerCommand ascendSlowly) {
        oi.driverGamepad.getifAvailable(1).whileHeld(ascend); // a
        oi.driverGamepad.getifAvailable(2).whileHeld(ascendSlowly); // b
    }

    @Inject
    public void setupCollectCubeCommandGroup(OperatorInterface oi, CollectCubeCommandGroup collectCube) {
        // oi.operatorGamepad.getifAvailable(7).whileHeld(collectCube);
    }

    @Inject
    public void setupVisionCommands(OperatorInterface oi, AcquireVisibleCubeCommand acquireCube,
            NavToTestGoalCommand testNav, DriveAtVelocityInfinitelyCommand driveAtVelLow,
            DriveAtVelocityInfinitelyCommand driveAtVelHigh,
            IdentifyAndPurePursuitToVisibleCubeCommand driveToLocalCubeCommand, ExtendRetractZedCommand extendZed,
            ExtendRetractZedCommand retractZed) {
        acquireCube.includeOnSmartDashboard();
        testNav.includeOnSmartDashboard();

        driveAtVelLow.setVelocity(20);
        driveAtVelLow.includeOnSmartDashboard("Test drive at velocity (low)");

        driveAtVelHigh.setVelocity(50);
        driveAtVelHigh.includeOnSmartDashboard("Test drive at velocity (high)");

        driveToLocalCubeCommand.setTimeoutPreset(TimeoutPreset.Manual);
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
            SetWristAngleCommand medium, SetWristAngleCommand high, WristDangerousOverrideCommand danger) {
        oi.operatorGamepad.getifAvailable(9).whenPressed(calibrate);
        loseCalibration.includeOnSmartDashboard();

        oi.operatorGamepad.getifAvailable(5).whileHeld(danger);

        low.setGoalAngle(0);
        medium.setGoalAngle(60);
        high.setGoalAngle(90);

        oi.operatorGamepad.getPovIfAvailable(270).whenPressed(maintain);
        oi.operatorGamepad.getPovIfAvailable(0).whenPressed(high);
        oi.operatorGamepad.getPovIfAvailable(90).whenPressed(medium);
        oi.operatorGamepad.getPovIfAvailable(180).whenPressed(low);
    }

    @Inject
    public void setupLowBatteryCommands(OperatorInterface oi, EnterLowBatteryModeCommand enter,
            LeaveLowBatteryModeCommand leave) {
        oi.driverGamepad.getifAvailable(9).whenPressed(enter);
        oi.driverGamepad.getifAvailable(10).whenPressed(leave);

        enter.includeOnSmartDashboard();
        leave.includeOnSmartDashboard();
    }
}
