package competition.subsystems.elevator.commands;

import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseCommand;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class TrackCurrentUsageCommand extends BaseCommand{
    
    ElevatorSubsystem elevator;
    XCANTalon talon;
    double oldCurrent;
    double current;
    double size;
    double total;
    double averageCurrent;
    double currentTime;
    double targetTime;
    double maxCurrentInAMinute;
    double minCurrentInAMinute;
    
    

    public TrackCurrentUsageCommand(XPropertyManager propMan,XCANTalon talon, ElevatorSubsystem elevator) {
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
    targetTime = Timer.getFPGATimestamp()+60;
    currentTime=Timer.getFPGATimestamp();
    maxCurrentInAMinute=current;
    minCurrentInAMinute=current;
    }


    @Override
    public void execute() {
       
        if (currentTime<targetTime) {
            
            current = talon.getOutputCurrent();
            
            if(oldCurrent != current) {
                total = total + current;
                size = size + 1;
                
                if (current > maxCurrentInAMinute) {
                    maxCurrentInAMinute = current;
                }

                if (current< minCurrentInAMinute) {
                    minCurrentInAMinute = current;
                }
                
            }
            averageCurrent = total / size;
            oldCurrent = current;
            currentTime=currentTime+1;
        }
        targetTime=currentTime+60;
    }
}
