package competition.subsystems.elevator.commands;

import com.google.inject.Inject;

import competition.operator_interface.OperatorInterface;
import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.injection.wpi_factories.CommonLibFactory;
import xbot.common.math.PIDFactory;
import xbot.common.properties.XPropertyManager;

public class OutreachElevatorMaintainerCommand extends ElevatorMaintainerCommand {

    @Inject
    public OutreachElevatorMaintainerCommand(ElevatorSubsystem elevator, PIDFactory pf, XPropertyManager propMan,
            OperatorInterface oi, CommonLibFactory clf) {
        super(elevator, pf, propMan, oi, clf);
    }

    @Override
    public double GetHumanInput() {
        // TODO Auto-generated method stub
        return super.GetHumanInput() * 0.25;
    }
}
