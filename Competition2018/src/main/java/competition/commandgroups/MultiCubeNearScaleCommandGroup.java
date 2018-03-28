package competition.commandgroups;

import java.util.List;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import competition.subsystems.drive.commands.AbsolutePurePursuit2018Command;
import competition.subsystems.drive.commands.TotalRobotPoint;
import competition.subsystems.elevator.ElevatorSubsystem;
import competition.subsystems.elevator.commands.MoveElevatorToHeightAndStabilizeCommand;
import competition.subsystems.elevator.commands.SetElevatorTargetHeightCommand;
import competition.subsystems.gripperintake.commands.GripperEjectCommand;
import competition.subsystems.gripperintake.commands.GripperIntakeCommand;
import competition.subsystems.gripperintake.commands.GripperStopCommand;
import competition.subsystems.shift.ShiftSubsystem.Gear;
import competition.subsystems.wrist.commands.SetWristAngleCommand;
import xbot.common.command.BaseCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.PurePursuitCommand.PointLoadingMode;
import xbot.common.subsystems.drive.RabbitPoint.PointDriveStyle;
import xbot.common.subsystems.drive.RabbitPoint.PointTerminatingType;
import xbot.common.subsystems.drive.RabbitPoint.PointType;
import xbot.common.subsystems.drive.RabbitPoint;

public class MultiCubeNearScaleCommandGroup extends BaseCommandGroup {

    public AbsolutePurePursuit2018Command pursuit;
    
    @Inject
    public MultiCubeNearScaleCommandGroup(
            AutonomousDecisionSystem decider,
            ElevatorSubsystem elevator,
            DelayViaSupplierCommand wait,
            AbsolutePurePursuit2018Command pursuit,
            SetWristAngleCommand setWristDown,
            SetWristAngleCommand setWristDownAgain,
            MoveElevatorToHeightAndStabilizeCommand setElevatorForScale,
            ConfigurablePurePursuitCommand scootForward,
            GripperEjectCommand eject,
            MoveElevatorToHeightAndStabilizeCommand lowerElevator,
            SetWristAngleCommand setWristUp,
            SetWristAngleCommand setWristUpAgain,
            AbsolutePurePursuit2018Command getCube,
            AbsolutePurePursuit2018Command returnToScale,
            GripperIntakeCommand intake,
            GripperStopCommand stopCollector
            ) {
        this.pursuit = pursuit;
        
        List<RabbitPoint> simplePoints = RabbitPoint.upgradeFieldPoseList(decider.createPathToNearbyScalePlate());
        pursuit.addPoint(new TotalRobotPoint(simplePoints.get(0), Gear.HIGH_GEAR, 120));
        pursuit.addPoint(new TotalRobotPoint(simplePoints.get(1), Gear.LOW_GEAR, 80));
                
        scootForward.addPoint(new RabbitPoint(new FieldPose(new XYPair(0, 2.666*12), new ContiguousHeading(90))));
        scootForward.setMode(PointLoadingMode.Relative);
        
        setWristDown.setGoalAngle(0);
        setWristDownAgain.setGoalAngle(0);
        setWristUp.setGoalAngle(45);
        setWristUpAgain.setGoalAngle(45);
        setElevatorForScale.setTargetHeight(elevator.getTargetScaleMidHeight());
        lowerElevator.setTargetHeight(elevator.getTargetPickUpHeight());
        wait.setDelaySupplier(() -> decider.getDelay());
        
        getCube.addPoint(new TotalRobotPoint(
                new RabbitPoint(
                        new FieldPose(new XYPair(0, -3*12), new ContiguousHeading(90)), 
                        PointType.PositionAndHeading, 
                        PointTerminatingType.Continue, 
                        PointDriveStyle.Macro),
                Gear.LOW_GEAR,
                80));
        
        getCube.addPoint(new TotalRobotPoint(
                new RabbitPoint(
                        new FieldPose(new XYPair(0, 0), new ContiguousHeading(225)), 
                        PointType.HeadingOnly, 
                        PointTerminatingType.Continue, 
                        PointDriveStyle.Macro),
                Gear.LOW_GEAR,
                80));
        
        getCube.addPoint(new TotalRobotPoint(
                new RabbitPoint(
                        new FieldPose(new XYPair(-3*12, -6*12), new ContiguousHeading(225)), 
                        PointType.PositionAndHeading, 
                        PointTerminatingType.Continue, 
                        PointDriveStyle.Micro),
                Gear.LOW_GEAR,
                60));
        
        
        
        returnToScale.addPoint(new TotalRobotPoint(
                new RabbitPoint(
                        new FieldPose(new XYPair(0*12, -6*12), new ContiguousHeading(180)), 
                        PointType.PositionAndHeading, 
                        PointTerminatingType.Continue, 
                        PointDriveStyle.Macro),
                Gear.LOW_GEAR,
                80));
        
        returnToScale.addPoint(new TotalRobotPoint(
                new RabbitPoint(
                        new FieldPose(new XYPair(0*12, 0*12), new ContiguousHeading(90)), 
                        PointType.HeadingOnly, 
                        PointTerminatingType.Continue, 
                        PointDriveStyle.Macro),
                Gear.LOW_GEAR,
                80));
        
        returnToScale.addPoint(new TotalRobotPoint(
                new RabbitPoint(
                        new FieldPose(new XYPair(0*12, 0*12), new ContiguousHeading(90)), 
                        PointType.PositionAndHeading, 
                        PointTerminatingType.Stop, 
                        PointDriveStyle.Macro),
                Gear.LOW_GEAR,
                80));
        
        
        /*
        this.addSequential(wait);
        
        // Get in position
        this.addSequential(pursuit);
        
        // Now we've stopped, so put the wrist down and the elevator up
        this.addParallel(setWristDown, 1);
        setElevatorForScale.changeTimeout(3.5);
        this.addSequential(setElevatorForScale);
        
        // scoot forward a little
        this.addSequential(scootForward);
        
        // Score for 1 second
        this.addSequential(eject, 1);
        
        */
        
        //this.addSequential(setWristUp, 0.33);
        this.addParallel(lowerElevator);
        
        // intake later
        this.addParallel(setWristDown, 1);
        this.addParallel(intake, 1);
        this.addSequential(getCube);
        
        this.addParallel(stopCollector);
        this.addParallel(setWristUpAgain, 1);
        this.addParallel(setElevatorForScale);
        this.addSequential(returnToScale);
        
        this.addParallel(setWristDownAgain, 1);
        //this.addSequential(setElevatorForScale);
        
        // eject
        this.addSequential(eject, 1);
        
    }
}
