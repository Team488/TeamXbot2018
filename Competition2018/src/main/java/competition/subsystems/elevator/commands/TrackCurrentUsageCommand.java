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
    double targetTime;
    double currentTime;
    double initialTime;
    double maxCurrentInAMinute;
    double minCurrentInAMinute;   



    public TrackCurrentUsageCommand(XPropertyManager propMan,XCANTalon talon, ElevatorSubsystem elevator) {
        this.elevator = elevator;
        this.talon = talon;
    }


    @Override
    public void initialize() {

        current=talon.getOutputCurrent();
        size = 1;
        total = current;
        initialTime=Timer.getFPGATimestamp();
        targetTime = initialTime+1;
        maxCurrentInAMinute=currentTime;
        minCurrentInAMinute=currentTime;

    }
    /**
     * can calculate the average current within a second and the max and min current within a minute;
     */

    @Override
    public void execute() {

        currentTime=Timer.getFPGATimestamp();

        while (currentTime<targetTime) {

            current = talon.getOutputCurrent(); 
            total = total + current;
            size = size + 1;

            //if it is beyond 60 seconds, the max and min number would go to zero; 
            if (currentTime-initialTime >= 60 ) {
                maxCurrentInAMinute=0;
                minCurrentInAMinute=0;

            }
            if (current > maxCurrentInAMinute) {
                maxCurrentInAMinute = current;
            }

            if (current< minCurrentInAMinute) {
                minCurrentInAMinute = current;
            }

            averageCurrent = total / size;
        }

        targetTime=currentTime+1;
    }
}
