package competition.subsystems.offboard;

public class MockOffboardCommsInterface implements XOffboardCommsInterface {

    @Override
    public void sendRaw(byte packetType, byte[] data) {
        // Intentionally left blank
    }

    @Override
    public OffboardCommunicationPacket receiveRaw() {
        return null;
    }

}
