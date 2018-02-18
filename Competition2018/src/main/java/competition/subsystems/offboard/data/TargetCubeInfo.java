package competition.subsystems.offboard.data;

import competition.subsystems.offboard.OffboardCommsConstants;

public class TargetCubeInfo {
    /**
     * The front-back component of the position
     */
    public final double xInches;
    
    /**
     * The left-right component of the position
     */
    public final double yInches;
    
    /**
     * The up-down component of the position
     */
    public final double zInches;

    public TargetCubeInfo(double xInches, double xInches, double xInches) {
        this.xInches = xInches;
        this.yInches = yInches;
        this.zInches = zInches;
    }
}
