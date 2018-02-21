package competition.subsystems.offboard.packets;

public class CommandFinishedPacket {
    public final int commandId;
    public final int statusCode;
    
    private CommandFinishedPacket(byte[] packetData) {
        if (packetData.length != 2) {
            throw new IllegalArgumentException();
        }
        
        // "& 0xFF" is needed to interpret byte as unsigned rather than signed:
        // https://stackoverflow.com/questions/7401550/how-to-convert-int-to-unsigned-byte-and-back
        this.commandId = packetData[0] & 0xFF;
        this.statusCode = packetData[1] & 0xff;
    }
    
    public static CommandFinishedPacket parse(byte[] packetData) {
        return new CommandFinishedPacket(packetData);
    }
}
