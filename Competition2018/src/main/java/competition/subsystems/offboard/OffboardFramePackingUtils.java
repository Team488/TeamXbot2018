package competition.subsystems.offboard;

public class OffboardFramePackingUtils {
    
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

    public static byte[] packDroneCommandFrame(double forward, double sideways, double up, double yaw) {
        short forwardInteger = (short)(forward * 1_000);
        short sidewaysInteger = (short)(sideways * 1_000);
        short upInteger = (short)(up * 1_000);
        short yawInteger = (short)(yaw * 1_000);
        
        return new byte[] {
            (byte)(forwardInteger >>> 8),
            (byte)(forwardInteger & 0xFF),
            (byte)(sidewaysInteger >>> 8),
            (byte)(sidewaysInteger & 0xFF),
            (byte)(upInteger >>> 8),
            (byte)(upInteger & 0xFF),
            (byte)(yawInteger >>> 8),
            (byte)(yawInteger & 0xFF)
        };
    }
}
