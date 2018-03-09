package competition.subsystems.offboard.packets;

import org.apache.log4j.Logger;

import competition.subsystems.gripperintake.GripperIntakeSubsystem.GripperMode;

public class GripperCommandPacket {
    
    private static Logger log = Logger.getLogger(GripperCommandPacket.class);
    
    public final int commandId;
    public final GripperMode gripperMode;
    
    private GripperCommandPacket(byte[] packetData) {
        if (packetData.length != 2) {
            throw new IllegalArgumentException();
        }
        
        this.commandId = packetData[0] & 0xFF;
        
        switch (packetData[1]) {
        case 0:
            gripperMode = GripperMode.Stop;
            break;
        case 1:
            gripperMode = GripperMode.Intake;
            break;
        case 2:
            gripperMode = GripperMode.Eject;
            break;
        default:
            log.error("Gripper Mode: " + packetData[1] + " is an invalid value");
            gripperMode = null;
            break;
        }
    }
    
    public static GripperCommandPacket parse(byte[] packetData) {
        return new GripperCommandPacket(packetData);
    }
}
