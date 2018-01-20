package competition.subsystems.offboard.packets;

public class DriveCommandPacket {
    public final int commandId;
    public final double leftPower;
    public final double rightPower;
    
    private DriveCommandPacket(byte[] packetData) {
        this.commandId = packetData[0] & 0xFF;
        this.leftPower = ((packetData[1] & 0xFF) << 8) | (packetData[2] & 0xFF);
        this.rightPower = ((packetData[3] & 0xFF) << 8) | (packetData[4] & 0xFF);
    }
    
    public static DriveCommandPacket parse(byte[] packetData) {
        return new DriveCommandPacket(packetData);
    }
}
