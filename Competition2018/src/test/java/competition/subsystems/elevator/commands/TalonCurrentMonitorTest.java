package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import xbot.common.controls.actuators.XCANTalon;
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;

public class TalonCurrentMonitorTest extends BaseCompetitionTest {

    TalonCurrentMonitor currentMonitor;
    XCANTalon talon;

    @Override
    public void setUp() {
        super.setUp();
        talon = clf.createCANTalon(1);
        currentMonitor = new TalonCurrentMonitor(talon);
    }

    @Test
    public void overAveragingWindowTest() {
        for (int i = 1; i <= currentMonitor.current_averaging_window; i++) {
            ((MockCANTalon) talon).setOutputCurrent(i);
            currentMonitor.measureAverageCurrent();
        }
        ((MockCANTalon) talon).setOutputCurrent(26);
        assertEquals(14, currentMonitor.measureAverageCurrent(), 1e-5);
        assertEquals(25, currentMonitor.currentHistory.size(), 0);
    }
    
    @Test
    public void atAveragingWindowTest() {
        for (int i = 1; i <= currentMonitor.current_averaging_window - 1; i++) {
            ((MockCANTalon) talon).setOutputCurrent(i);
            currentMonitor.measureAverageCurrent();
        }
        ((MockCANTalon) talon).setOutputCurrent(25);
        assertEquals(13, currentMonitor.measureAverageCurrent(), 1e-5);
        assertEquals(25, currentMonitor.currentHistory.size(), 0);
    }
    
    @Test
    public void underAveragingWindowTest() {
        for (int i = 1; i <= currentMonitor.current_averaging_window - 2; i++) {
            ((MockCANTalon) talon).setOutputCurrent(i);
            currentMonitor.measureAverageCurrent();
        }
        ((MockCANTalon) talon).setOutputCurrent(24);
        assertEquals(12.5, currentMonitor.measureAverageCurrent(), 1e-5);
        assertEquals(24, currentMonitor.currentHistory.size(), 0);
    }

    @Test
    public void trackPeakCurrentOverAveragingWindowTest() {
        for (int i = 1; i <= currentMonitor.current_averaging_window; i++) {
            ((MockCANTalon) talon).setOutputCurrent(i);
        }
        ((MockCANTalon) talon).setOutputCurrent(26);
        assertEquals(26, currentMonitor.peakCurrent(), 1e-5);
        }
    
    @Test
    public void trackPeakCurrentEqualAveragingWindowTest() {
        for (int i = 1; i <= currentMonitor.current_averaging_window; i++) {
            ((MockCANTalon) talon).setOutputCurrent(i);
        }
        ((MockCANTalon) talon).setOutputCurrent(25);
        assertEquals(25, currentMonitor.peakCurrent(), 1e-5);
        }
    
    @Test
    public void trackPeakCurrentUnderAveragingWindowTest() {
        for (int i = 1; i <= currentMonitor.current_averaging_window; i++) {
            ((MockCANTalon) talon).setOutputCurrent(i);
        }
        ((MockCANTalon) talon).setOutputCurrent(24);
        assertEquals(24, currentMonitor.peakCurrent(), 1e-5);
        }
    }


