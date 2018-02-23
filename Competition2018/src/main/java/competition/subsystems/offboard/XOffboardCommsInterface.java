package competition.subsystems.offboard;

public interface XOffboardCommsInterface {
    public void sendRaw(byte packetType, byte[] data);
    public OffboardCommunicationPacket receiveRaw();
}
