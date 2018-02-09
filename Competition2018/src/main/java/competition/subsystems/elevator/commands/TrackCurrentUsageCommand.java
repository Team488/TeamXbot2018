package competition.subsystems.elevator.commands;

import java.util.HashMap;

import edu.wpi.first.wpilibj.Timer;
import xbot.common.command.BaseCommand;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.properties.XPropertyManager;

public class TrackCurrentUsageCommand extends BaseCommand {

    XCANTalon talon;
    double current;
    double time;
    double currentAveragingWindow;

    HashMap<Double, Double> currentHistory;

    public TrackCurrentUsageCommand(XPropertyManager propMan, XCANTalon talon) {
        this.talon = talon;
        currentHistory = new HashMap<Double, Double>();
        currentAveragingWindow = 1000;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        current = talon.getOutputCurrent();
        time = Timer.getFPGATimestamp();
        currentHistory.put(time, current);
        for (Double presentTime : currentHistory.keySet()) {
            if (presentTime < time - currentAveragingWindow) {
                currentHistory.remove(presentTime);
            }
        }
    }

    public double getAverageCurrent() {
        double sum = 0;
        for (Double presentTime : currentHistory.keySet()) {
            sum += currentHistory.get(presentTime);
        }
        return sum / currentHistory.size();
    }
    
    public double getPeakCurrent() {
        double peakCurrent = talon.getOutputCurrent();
        for(Double presentTime : currentHistory.keySet()) {
            if(peakCurrent < currentHistory.get(presentTime)) {
                peakCurrent = currentHistory.get(presentTime);
            }
        }
        return peakCurrent;
    }
}
