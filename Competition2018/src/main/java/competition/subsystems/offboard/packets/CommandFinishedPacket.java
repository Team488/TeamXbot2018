package competition.subsystems.offboard.packets;

public class CommandFinishedPacket {
    public final int commandId;
    public final int statusCode;
    
    private CommandFinishedPacket(byte[] packetData) {
        this.commandId = packetData[0] & 0xFF;
        this.statusCode = packetData[1] & 0xff;
    }
    
    public static CommandFinishedPacket parse(byte[] packetData) {
        return new CommandFinishedPacket(packetData);
    }
}
