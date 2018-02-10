package competition.subsystems.offboard;

public class OffboardFramePackingUtils {
    
    public static byte[] packWheelOdomFrame(double leftDriveDelta, double rightDriveDelta, double timeDelta) {
        short leftDriveDeltaInteger = (short)(leftDriveDelta * 1_000);
        short rightDriveDeltaInteger = (short)(rightDriveDelta * 1_000);
        // TODO: check for time overflow
        // TODO: unsigned
        short timeDeltaInteger = (short)(timeDelta * 10_000);
        
        return new byte[] {
            (byte)(leftDriveDeltaInteger >>> 8),
            (byte)(leftDriveDeltaInteger & 0xFF),
            (byte)(rightDriveDeltaInteger >>> 8),
            (byte)(rightDriveDeltaInteger & 0xFF),
            (byte)(timeDeltaInteger >>> 8),
            (byte)(timeDeltaInteger & 0xFF)
        };
    }
    
    public static byte[] packSetCommandFrame(int commandId) {
        return new byte[] {
        		(byte)(commandId & 0xFF)
        };
    }
    
    public static byte[] packScoringPlacement(int sequence) {
    	return new byte[] {
    			(byte)(sequence & 0xFF)	
    	};
    }
    
    public static byte[] packHeadingFrame(double headingDegrees) {
        short headingInteger = (short)(headingDegrees * 100);
        
        return new byte[] {
        		(byte)(headingInteger >>> 8),
        		(byte)(headingInteger & 0xFF)
        };
    }

}
