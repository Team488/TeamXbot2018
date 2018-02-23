package competition.subsystems.offboard;

public class OffboardCommunicationPacket {
    public final byte packetType;
    public final byte[] data;
    
    public OffboardCommunicationPacket(byte packetType, byte[] data) {
        this.packetType = packetType;
        this.data = data;
    }
}