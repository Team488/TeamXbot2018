package competition.operator_interface;

import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.commands.AcquireVisibleCubeCommand;
import competition.subsystems.offboard.commands.NavToTestGoalCommand;
import competition.subsystems.offboard.data.TargetCubeInfo;
import competition.subsystems.pose.PoseSubsystem;
import competition.commandgroups.CollectCubeCommandGroup;
import competition.commandgroups.DynamicScoreOnSwitchCommandGroup;
import competition.subsystems.autonomous.commands.DriveNowhereCommand;
import competition.subsystems.climb.commands.AscendClimberCommand;
import competition.subsystems.climb.commands.DecendClimberCommand;
import competition.subsystems.climberdeploy.commands.ExtendClimberArmCommand;
import competition.subsystems.climberdeploy.commands.RetractClimberArmCommand;
import competition.subsystems.drive.commands.AssistedTankDriveCommand;
import competition.subsystems.drive.commands.DriveAtVelocityInfinitelyCommand;
import competition.subsystems.drive.commands.DriveForDistanceCommand;
import competition.subsystems.drive.commands.TankDriveWithJoysticksCommand;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.CalibrateElevatorHereCommand;
import competition.subsystems.elevator.commands.CalibrateElevatorTicksPerInchCommand;
import competition.subsystems.elevator.commands.DisableElevatorCurrentLimitCommand;
import competition.subsystems.elevator.commands.ElevatorMaintainerCommand;
import competition.subsystems.elevator.commands.ElevatorUncalibrateCommand;
import competition.subsystems.elevator.commands.EnableElevatorCurrentLimitCommand;
import competition.subsystems.elevator.commands.ExperimentMotionMagicCommand;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.gripperintake.commands.GripperRotateClockwiseCommand;
import competition.subsystems.gripperintake.commands.GripperRotateCounterClockwiseCommand;
import competition.subsystems.shift.commands.ShiftHighCommand;
import competition.subsystems.shift.commands.ShiftLowCommand;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import competition.subsystems.wrist.commands.WristCalibrateCommand;
import competition.subsystems.wrist.commands.WristMaintainerCommand;
import competition.subsystems.wrist.commands.WristUncalibrateCommand;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.properties.ConfigurePropertiesCommand;
import xbot.common.subsystems.drive.PurePursuitCommand;
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
    public void setupMiscCommands(ConfigurePropertiesCommand fastMode,
            ConfigurePropertiesCommand slowMode) {
        fastMode.setFastMode(true);
        slowMode.setFastMode(false);
        
        fastMode.includeOnSmartDashboard();
        slowMode.includeOnSmartDashboard();
    }

    @Inject
    public void setupDriveCommands(OperatorInterface oi, AssistedTankDriveCommand assistedTank,
            TankDriveWithJoysticksCommand simpleTank,
            PurePursuitCommand pursuit,
            ResetDistanceCommand resetDistance,
            SetRobotHeadingCommand setHeading,
            DynamicScoreOnSwitchCommandGroup dynamicScore) {
        oi.driverGamepad.getifAvailable(9).whenPressed(assistedTank);
        oi.driverGamepad.getifAvailable(10).whenPressed(simpleTank);
        
        pursuit.addPoint(new FieldPose(new XYPair(0, 45), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-45, 90), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(0, 135), new ContiguousHeading(-90)));
        pursuit.addPoint(new FieldPose(new XYPair(0, 45), new ContiguousHeading(-90)));
        
        pursuit.includeOnSmartDashboard();
        
        resetDistance.includeOnSmartDashboard();
        setHeading.setHeadingToApply(90);
        setHeading.includeOnSmartDashboard();
        
        dynamicScore.includeOnSmartDashboard();
    }
    
    @Inject
    public void simpleDriveCommandStartToOuterCloseSwitch(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(2.3*12, 12.5*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(-2*12, 13*12), new ContiguousHeading(180)));
        pursuit.includeOnSmartDashboard("Outer close switch");
    }
    
    /**
     *    @Inject
    public void simpleDriveCommandStartToOuterCloseSwitch(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(1*12, 4*12), new ContiguousHeading(60)));
        pursuit.addPoint(new FieldPose(new XYPair(1.7*12, 6*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(2.3*12, 9.5*12), new ContiguousHeading(120)));
        pursuit.addPoint(new FieldPose(new XYPair(1.3*12, 12.5*12), new ContiguousHeading(150)));
        pursuit.addPoint(new FieldPose(new XYPair(-2*12, 13*12), new ContiguousHeading(180)));
        pursuit.includeOnSmartDashboard("Outer close switch");
    }
     */
    
    @Inject
    public void simpleDriveCommandOuterCloseSwitchToCloseScale(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 13*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 17*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-3.5*12, 17*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(-3.5*12, 17*12), new ContiguousHeading(270)));
        pursuit.addPoint(new FieldPose(new XYPair(-3.5*12, 15.5*12), new ContiguousHeading(270)));
        pursuit.addPoint(new FieldPose(new XYPair(-3.5*12, 18.5*12), new ContiguousHeading(270)));
        pursuit.addPoint(new FieldPose(new XYPair(1*12, 20*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-2*12, 25*12), new ContiguousHeading(180)));
        pursuit.includeOnSmartDashboard("Outer close switch to Outer close scale");
    }
    
    @Inject
    public void simpleDriveCommandOuterCloseSwitchToFarScale(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 13*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 17*12), new ContiguousHeading(135)));
        pursuit.addPoint(new FieldPose(new XYPair(-15.85*12, 17*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(-15.85*12, 22.5*12), new ContiguousHeading(90)));

        pursuit.includeOnSmartDashboard("Outer close switch to Outer far scale");
    }
    
    @Inject
    public void simpleDriveCommandStartToInnerCloseSwitch(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-7*12, 8.5*12), new ContiguousHeading(90)));

        pursuit.includeOnSmartDashboard("Inner close switch");
    }
    
    /**
     *     @Inject
    public void simpleDriveCommandStartToInnerCloseSwitch(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-2*12, 3*12), new ContiguousHeading(135)));
        pursuit.addPoint(new FieldPose(new XYPair(-4*12, 5.3*12), new ContiguousHeading(135)));
        pursuit.addPoint(new FieldPose(new XYPair(-7*12, 8.5*12), new ContiguousHeading(90)));

        pursuit.includeOnSmartDashboard("Inner close switch");
    }
     */
    
    @Inject
    public void simpleDriveCommandStartToInnerFarSwitch(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-13.85*12, 3*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(-15.3*12, 8.5*12), new ContiguousHeading(90)));

        pursuit.includeOnSmartDashboard("Inner far switch");
    }
    
    /**
     *  @Inject
    public void simpleDriveCommandStartToInnerFarSwitch(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-1.35*12, 2.5*12), new ContiguousHeading(127.5)));
        pursuit.addPoint(new FieldPose(new XYPair(-5.35*12, 3*12), new ContiguousHeading(160)));
        pursuit.addPoint(new FieldPose(new XYPair(-7.35*12, 4*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(-10.35*12, 4*12), new ContiguousHeading(165)));
        pursuit.addPoint(new FieldPose(new XYPair(-12.35*12, 5*12), new ContiguousHeading(150)));
        pursuit.addPoint(new FieldPose(new XYPair(-13.85*12, 7*12), new ContiguousHeading(120)));
        pursuit.addPoint(new FieldPose(new XYPair(-15.3*12, 8.5*12), new ContiguousHeading(90)));

        pursuit.includeOnSmartDashboard("Inner far switch");
    }
     */
    
    @Inject
    public void simpleDriveCommandInnerFarSwitchToInnerFarScale(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(-15.3*12, 6.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-20.3*12, 8.5*12), new ContiguousHeading(135)));
        pursuit.addPoint(new FieldPose(new XYPair(-18.8*12, 19*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-17.3*12, 16*12), new ContiguousHeading(315)));
        pursuit.addPoint(new FieldPose(new XYPair(-15.3*12, 22.5*12), new ContiguousHeading(0)));

        pursuit.includeOnSmartDashboard("Inner far switch to Inner far scale");
    }
    
    @Inject
    public void simpleDriveCommandInnerFarSwitchToInnerCloseScale(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(-15.3*12, 6.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-20.3*12, 8.5*12), new ContiguousHeading(135)));
        pursuit.addPoint(new FieldPose(new XYPair(-18.8*12, 19*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-17.3*12, 16*12), new ContiguousHeading(315)));
        pursuit.addPoint(new FieldPose(new XYPair(-18.3*12, 17*12), new ContiguousHeading(315)));
        pursuit.addPoint(new FieldPose(new XYPair(-12*12, 17*12), new ContiguousHeading(0)));
        pursuit.addPoint(new FieldPose(new XYPair(-7*12, 122.5*12), new ContiguousHeading(90)));



        pursuit.includeOnSmartDashboard("Inner far switch to Inner far scale");
    }
    
    @Inject
    public void simpleDriveCommandStartToOuterCloseScale(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(1.15*12, 24*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(-2.1*12, 25*12), new ContiguousHeading(180)));

        pursuit.includeOnSmartDashboard("Outer close scale");
    }
    
    /**
     *     @Inject
    public void simpleDriveCommandStartToOuterCloseScale(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(1.15*12, 3*12), new ContiguousHeading(45)));
        pursuit.addPoint(new FieldPose(new XYPair(1.65*12, 6*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(1.65*12, 13*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(1.65*12, 18*12), new ContiguousHeading(112.5)));
        pursuit.addPoint(new FieldPose(new XYPair(1.15*12, 22*12), new ContiguousHeading(135)));
        pursuit.addPoint(new FieldPose(new XYPair(.15*12, 24*12), new ContiguousHeading(157.5)));
        pursuit.addPoint(new FieldPose(new XYPair(-2.1*12, 25*12), new ContiguousHeading(180)));

        pursuit.includeOnSmartDashboard("Outer close scale");
    }
     */
    
    @Inject
    public void simpleDriveCommandStartToInnerFarScale(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-1*12, 17.5*12), new ContiguousHeading(180)));
        pursuit.addPoint(new FieldPose(new XYPair(-15.85*12, 19*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(-15.85*12, 22.5*12), new ContiguousHeading(90)));

        pursuit.includeOnSmartDashboard("Inner far scale");
    }
    
    /**
     *     @Inject
    public void simpleDriveCommandStartToInnerFarScale(PurePursuitCommand pursuit) {
        pursuit.addPoint(new FieldPose(new XYPair(0*12, 1.5*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(1.65*12, 6*12), new ContiguousHeading(60)));
        pursuit.addPoint(new FieldPose(new XYPair(1.65*12, 15*12), new ContiguousHeading(90)));
        pursuit.addPoint(new FieldPose(new XYPair(.65*12, 19*12), new ContiguousHeading(120)));
        pursuit.addPoint(new FieldPose(new XYPair(-1.35*12, 20*12), new ContiguousHeading(155)));
        pursuit.addPoint(new FieldPose(new XYPair(-7.35*12, 19*12), new ContiguousHeading(210)));
        pursuit.addPoint(new FieldPose(new XYPair(-12.35*12, 18*12), new ContiguousHeading(155)));
        pursuit.addPoint(new FieldPose(new XYPair(-14.85*12, 19*12), new ContiguousHeading(120)));
        pursuit.addPoint(new FieldPose(new XYPair(-15.85*12, 22.5*12), new ContiguousHeading(90)));

        pursuit.includeOnSmartDashboard("Inner far scale");
    }
     */

    @Inject
    public void setupShiftGearCommand(OperatorInterface oi, ShiftHighCommand shiftHigh, ShiftLowCommand shiftLow) {

        oi.driverGamepad.getifAvailable(5).whenPressed(shiftLow);
        oi.driverGamepad.getifAvailable(6).whenPressed(shiftHigh);
    }

    @Inject
    public void setupGripperCommands(OperatorInterface oi, GripperRotateClockwiseCommand clockwise,
            GripperRotateCounterClockwiseCommand counterClockwise,
            GripperEjectCommand eject, GripperIntakeCommand intake) {
        oi.operatorGamepad.getAnalogIfAvailable(oi.gripperEject).whileHeld(eject);
        oi.operatorGamepad.getAnalogIfAvailable(oi.gripperIntake).whileHeld(intake);
        oi.operatorGamepad.getifAvailable(7).whileHeld(counterClockwise);
        oi.operatorGamepad.getifAvailable(8).whileHeld(clockwise);
    }

    @Inject
    public void setupElevatorCommands(
            OperatorInterface oi,
            CalibrateElevatorTicksPerInchCommand calibrateElevatorTicks,
            ElevatorUncalibrateCommand uncalibrate,
            ElevatorMaintainerCommand maintainer,
            SetElevatorTargetHeightCommand targetScaleHighHeight,
            SetElevatorTargetHeightCommand targetScaleMidHeight,
            SetElevatorTargetHeightCommand targetSwitchDropHeight,
            SetElevatorTargetHeightCommand targetPickUpHeight,
            CalibrateElevatorHereCommand calibrateHere,
            EnableElevatorCurrentLimitCommand enableCurrentLimit,
            DisableElevatorCurrentLimitCommand disableCurrentLimit,
            ExperimentMotionMagicCommand mm,
            ElevatorSubsystem elevatorSubsystem) {
        oi.operatorGamepad.getifAvailable(5).whileHeld(calibrateElevatorTicks);
        oi.operatorGamepad.getifAvailable(6).whenPressed(maintainer);

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
    public void setupClimberCommands(OperatorInterface oi, AscendClimberCommand ascend, DecendClimberCommand decend,
            ExtendClimberArmCommand extendArm, RetractClimberArmCommand retractArm) {
        oi.driverGamepad.getifAvailable(1).whileHeld(extendArm);
        oi.driverGamepad.getifAvailable(2).whileHeld(retractArm);
        oi.driverGamepad.getAnalogIfAvailable(oi.raiseClimber).whileActive(ascend);
        oi.driverGamepad.getAnalogIfAvailable(oi.lowerClimber).whileActive(decend);
    }

    @Inject
    public void setupCollectCubeCommandGroup(OperatorInterface oi, CollectCubeCommandGroup collectCube) {
        //oi.operatorGamepad.getifAvailable(7).whileHeld(collectCube);
    }
    
    @Inject
    public void setupVisionCommands(
                OperatorInterface oi,
                AcquireVisibleCubeCommand acquireCube,
                NavToTestGoalCommand testNav,
                DriveAtVelocityInfinitelyCommand driveAtVel,
                PurePursuitCommand driveToLocalCubeCommand,
                OffboardInterfaceSubsystem offboardSubsystem,
                PoseSubsystem poseSubsystem) {
        acquireCube.includeOnSmartDashboard();
        testNav.includeOnSmartDashboard();
        driveAtVel.includeOnSmartDashboard();

        driveToLocalCubeCommand.setMode(PursuitMode.Relative);
        driveToLocalCubeCommand.setPointSupplier(() -> {
            TargetCubeInfo targetCube = offboardSubsystem.getTargetCube();
            if(targetCube == null) {
                return null;
            }
            double headingDelta = Math.toDegrees(Math.atan2(targetCube.xInches, targetCube.yInches));

            FieldPose targetPose = new FieldPose(new XYPair(targetCube.xInches, targetCube.yInches), new ContiguousHeading(90 - headingDelta));
            return Arrays.asList(targetPose);
        });
        
        driveToLocalCubeCommand.includeOnSmartDashboard("Drive to local cube");
    }

    @Inject
    public void setupWristCommands(OperatorInterface oi, WristCalibrateCommand calibrate,
            WristUncalibrateCommand loseCalibration,
            WristMaintainerCommand maintain,
            SetWristAngleCommand low,
            SetWristAngleCommand medium,
            SetWristAngleCommand high) {
        oi.operatorGamepad.getifAvailable(9).whenPressed(calibrate);
        loseCalibration.includeOnSmartDashboard();
        
        low.setGoalAngle(0);
        medium.setGoalAngle(45);
        high.setGoalAngle(90);
        
        oi.operatorGamepad.getPovIfAvailable(270).whenPressed(maintain);
        oi.operatorGamepad.getPovIfAvailable(0).whenPressed(high);
        oi.operatorGamepad.getPovIfAvailable(90).whenPressed(medium);
        oi.operatorGamepad.getPovIfAvailable(180).whenPressed(low);
    }

    @Inject
    public void setupAutonomousCommands(OperatorInterface oi, DriveNowhereCommand nowhere,
            DriveForDistanceCommand drive5Ft) {
        drive5Ft.setDeltaDistance(60);
        drive5Ft.includeOnSmartDashboard();
        nowhere.includeOnSmartDashboard();
    }
}
