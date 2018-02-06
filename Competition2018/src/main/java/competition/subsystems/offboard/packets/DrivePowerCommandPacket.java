package competition.subsystems.offboard.packets;

public class DrivePowerCommandPacket {
    public final int commandId;
    public final double leftPower;
    public final double rightPower;
    
    private DrivePowerCommandPacket(byte[] packetData) {
        this.commandId = packetData[0] & 0xFF;
        this.leftPower = parseSinglePower(packetData[1], packetData[2]);
        this.rightPower = parseSinglePower(packetData[3], packetData[4]);
    }
    
    public static double parseSinglePower(byte firstByte, byte secondByte) {
        int total = (firstByte << 8) | (secondByte & 0xFF);
        return total / 3000d;
    }
    
    public static DrivePowerCommandPacket parse(byte[] packetData) {
        return new DrivePowerCommandPacket(packetData);
    }
}
