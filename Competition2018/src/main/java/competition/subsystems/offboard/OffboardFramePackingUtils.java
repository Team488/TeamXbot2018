package competition.subsystems.offboard;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import openrio.powerup.MatchData;

public class OffboardFramePackingUtils {
    private static Logger log = Logger.getLogger(OffboardFramePackingUtils.class);

    public static byte[] packWheelOdomFrame(double leftDriveDelta, double rightDriveDelta, double timeDelta) {
        short leftDriveDeltaInteger = (short)(leftDriveDelta * 1_000);
        short rightDriveDeltaInteger = (short)(rightDriveDelta * 1_000);
        // TODO: Make this unsigned
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
    
    public static byte[] packScoringPlacement() {
        //Sequence - Near, Scale, Far
        //Left - 1, Right - 0
        ArrayList<Integer> scoringSides = new ArrayList<Integer>();
        for(MatchData.GameFeature feature: MatchData.GameFeature.values()) {
            if(MatchData.getOwnedSide(feature).equals(MatchData.OwnedSide.LEFT)) {
                scoringSides.add(1);
            }
            else if(MatchData.getOwnedSide(feature).equals(MatchData.OwnedSide.RIGHT)) {
                scoringSides.add(0);
            }
            else {
                log.info("The received scoring objective does not have valid side");
            }
        }
        int sequence = 0;
        //Sent as Far,Scale,Near
        for(int i = 2; i <= 0; i--) {
            int scoring = (scoringSides.get(i) << (i));
            sequence = (scoring | sequence);
        }
        return new byte[] {
                (byte)(sequence & 0xFF)    
        };
    }
    
}