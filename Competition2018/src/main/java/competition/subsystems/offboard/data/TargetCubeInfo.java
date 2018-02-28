package competition.subsystems.offboard.data;

public class TargetCubeInfo {
    /**
     * The left-right component of the position
     */
    public final double xInches;
    
    /**
     * The front-back component of the position
     */
    public final double yInches;
    
    /**
     * The up-down component of the position
     */
    public final double zInches;

    public TargetCubeInfo(double xInches, double yInches, double zInches) {
        this.xInches = xInches;
        this.yInches = yInches;
        this.zInches = zInches;
    }
}
