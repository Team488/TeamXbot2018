package competition.commandgroups;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousDecisionSystem;
import xbot.common.command.BaseCommandGroup;
import xbot.common.command.DelayViaSupplierCommand;
import xbot.common.math.ContiguousHeading;
import xbot.common.math.FieldPose;
import xbot.common.math.XYPair;
import xbot.common.subsystems.drive.ConfigurablePurePursuitCommand;
import xbot.common.subsystems.drive.RabbitPoint;
import xbot.common.subsystems.drive.RabbitPointBuilder;

public class CrossAutoLineCommandGroup extends BaseCommandGroup {

    @Inject
    public CrossAutoLineCommandGroup(
            AutonomousDecisionSystem decider,
            DelayViaSupplierCommand delay,
            ConfigurablePurePursuitCommand crossLine) {
                
        delay.setDelaySupplier(() -> decider.getDelay());
        crossLine.addPoint(new RabbitPoint(new FieldPose(new XYPair(0, 7.5*12), new ContiguousHeading(90))));
        
        this.addSequential(delay);
        this.addSequential(crossLine);
    }
    
}
