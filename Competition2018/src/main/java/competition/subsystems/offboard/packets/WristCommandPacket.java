package competition.subsystems.offboard.packets;

import org.apache.log4j.Logger;

import competition.subsystems.wrist.WristSubsystem.WristPosition;

public class WristCommandPacket {

    private static Logger log = Logger.getLogger(WristCommandPacket.class);
    
    public final int commandId;
    public final WristPosition position;
    
    private WristCommandPacket(byte[] packetData) {
        if (packetData.length != 2) {
            throw new IllegalArgumentException();
        }
        
        this.commandId = packetData[0] & 0xFF;
        
        switch (packetData[1]) {
        case 0:
            this.position = WristPosition.Down;
            break;
        case 1:
            this.position = WristPosition.Up;
            break;
        default:
            this.position = null;
            log.error("Wrist Position: " + packetData[1] + " is an invalid value");
            break;
        }
    }
    
    public static WristCommandPacket parse(byte[] packetData) {
        return new WristCommandPacket(packetData);
    }
}
