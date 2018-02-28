package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseSetpointCommand;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class MoveElevatorToHeightAndStabilizeCommand extends BaseSetpointCommand {
    private ElevatorSubsystem elevator;

    public DoubleProperty errorThreshold;
    public DoubleProperty stableTimeRequired;

    double initialTime;
    public double target;

    boolean previouslyAtTarget;

    @Inject
    public MoveElevatorToHeightAndStabilizeCommand(ElevatorSubsystem subsystem, XPropertyManager propMan) {
        super(subsystem);
        this.elevator = subsystem;
        errorThreshold = propMan.createPersistentProperty("Elevator delta height threshold", 0.1);
        stableTimeRequired = propMan.createPersistentProperty("Required time on target for elevator in seconds", 0.5);
        previouslyAtTarget = false;
    }

    public void setTargetHeight(double height) {
        this.target = height;
    }

    @Override
    public void initialize() {
        log.info("Initializing at target height" + target);
    }

    @Override
    public void execute() {
        elevator.setTargetHeight(target);
        double error = Math.abs(elevator.getCurrentHeightInInches() - elevator.getTargetHeight());
        if (error <= errorThreshold.get()) {
            if (!previouslyAtTarget) {
                initialTime = Timer.getFPGATimestamp();
                previouslyAtTarget = true;
            }
        } else {
            previouslyAtTarget = false;
        }
    }

    @Override
    public boolean isFinished() {
        double currentTime = Timer.getFPGATimestamp();
        return previouslyAtTarget && (currentTime - initialTime) >= stableTimeRequired.get();
    }

}
