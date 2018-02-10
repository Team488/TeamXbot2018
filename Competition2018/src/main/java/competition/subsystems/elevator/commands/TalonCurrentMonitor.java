package competition.subsystems.elevator.commands;

import java.util.ArrayDeque;
import xbot.common.controls.actuators.XCANTalon;

public class TalonCurrentMonitor {

    XCANTalon talon;
    final int CURRENT_AVERAGING_WINDOW;
    ArrayDeque<Double> currentHistory;

    public TalonCurrentMonitor(XCANTalon talon) {
        this.talon = talon;
        currentHistory = new ArrayDeque<Double>();
        CURRENT_AVERAGING_WINDOW = 25;
    }

    public double measureAverageCurrent() {
        currentHistory.addFirst(talon.getOutputCurrent());
        if(currentHistory.size() > CURRENT_AVERAGING_WINDOW) {
            currentHistory.removeLast();
        }
        double sum = 0;
        for(Double current:currentHistory) {
            sum += current;
        }
        return sum / currentHistory.size();
    }
}
