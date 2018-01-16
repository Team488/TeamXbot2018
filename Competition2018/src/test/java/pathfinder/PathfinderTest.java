package pathfinder;

import java.io.File;

import org.junit.Test;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import xbot.common.injection.BaseWPITest;

public class PathfinderTest extends BaseWPITest {

    
    @Test
    public void generateAndSaveData() {
        Waypoint[] points = new Waypoint[] {
            new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
            new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
            new Waypoint(0, 0, 0)    
        };
        
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Trajectory trajectory = Pathfinder.generate(points, config);  
        
        TankModifier modifier = new TankModifier(trajectory).modify(0.5);
        
        File myFile = new File("myfile.csv");
        Pathfinder.writeToCSV(myFile, trajectory);
    }
}
