package competition;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.subsystems.pose.PoseSubsystem;


public class TiltPreventionModuleTest extends BaseCompetitionTest{

    TiltPreventionModule module;
    PoseSubsystem pose;

    @Override
    public void setUp() {
        super.setUp();
        this.pose = injector.getInstance(PoseSubsystem.class);
        this.module = injector.getInstance(TiltPreventionModule.class);
    }

    @Test
    public void verifyPositiveTiltAndPositivePower() {
        mockRobotIO.setGyroPitch(module.pitchThrehold.get()+5);
        assertEquals(0.5, module.preventTilt(0.5), 1e-5);
    }

    @Test
    public void verifyPositiveTiltAndNegativePower() {
        mockRobotIO.setGyroPitch(module.pitchThrehold.get()+5);
        System.out.println(mockRobotIO);
        System.out.println(pose.getRobotPitch());
        assertEquals(0, module.preventTilt(-0.5), 1e-5);
    }

    @Test
    public void verifyNegativeTiltAndPositivePower() {
        mockRobotIO.setGyroPitch(-module.pitchThrehold.get()-5);
        assertEquals(-0.5, module.preventTilt(-0.5), 1e-5);
    }

    @Test
    public void verifyNegativeTiltAndNegativePower() {
        mockRobotIO.setGyroPitch(-module.pitchThrehold.get()-5);
        assertEquals(0, module.preventTilt(0.5), 1e-5);
    }

    @Test
    public void verifyPowerInBetween() {
        mockRobotIO.setGyroPitch(module.pitchThrehold.get()-1);
        assertEquals(0.5, module.preventTilt(0.5), 1e-5);
    }
}
