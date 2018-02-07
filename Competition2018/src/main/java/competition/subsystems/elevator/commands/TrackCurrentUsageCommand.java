package competition.subsystems.elevator.commands;

import competition.subsystems.elevator.ElevatorSubsystem;
import xbot.common.command.BaseCommand;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TrackAverageCurrentCommand extends BaseCommand{
    
    ElevatorSubsystem elevator;
    XCANTalon talon;
    double oldCurrent;
    double current;
    double size;
    double total;
    double averageCurrent;
    
    

    public TrackAverageCurrentCommand(XPropertyManager propMan,XCANTalon talon, ElevatorSubsystem elevator) {
        this.elevator = elevator;
        this.talon = talon;
        DoubleProperty averageCurrent = propMan.createPersistentProperty("Average Current", 0);
        //DoubleProperty
        
    }
        

    @Override
    public void initialize() {
    oldCurrent = current;
    size = 1;
    total = current;
        
    }

    @Override
    public void execute() {
        current = talon.getOutputCurrent();
        
        if(oldCurrent != current) {
            total = total + current;
            size = size + 1;
        }
        averageCurrent = total / size;
        oldCurrent = current;
    }
}
