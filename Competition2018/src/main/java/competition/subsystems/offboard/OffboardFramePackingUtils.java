package competition.subsystems.offboard;

import java.util.ArrayList;

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
    
    public static byte[] packScoringPlacement(ArrayList<Integer> scoringPlacement) {
    	int sequence = 0;
    	for(int i = 0; i < 3; i++) {
    		int scoring = (scoringPlacement.get(i) << (2-i));
    		sequence = (scoring | sequence);
    	}
    	return new byte[] {
                (byte)(sequence & 0xFF)    
        };
    }
    
}