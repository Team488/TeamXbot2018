package competition.subsystems.autonomous;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.autonomous.AutonomousCommandSupplier.AutonomousMetaprogram;

public class AutonomousCommandSupplierTest extends BaseCompetitionTest {

    AutonomousCommandSupplier autoSupplier;
    
    @Override
    public void setUp() {
        super.setUp();
        autoSupplier = injector.getInstance(AutonomousCommandSupplier.class);
    }
    
    @Test
    public void noAutoSet() {
        assertTrue("If we never set the metaprogram, it should default to do nothing",
                autoSupplier.getAutoSupplier().get().getName().equals("DriveNowhereCommand"));
    }
    
    @Test
    public void setSwitch() {
        autoSupplier.setMetaprogram(AutonomousMetaprogram.Switch);
        
        assertTrue("Since we set switch, we should get switch auto",
                autoSupplier.getAutoSupplier().get().getName().equals("ScoreOnSwitchCommandGroup"));
    }
}
