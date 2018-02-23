package competition.subsystems.offboard.packets;

public class DriveVelCommandPacket {
    public final int commandId;
    public final double leftVelocityMetersPerSecond;
    public final double rightVelocityMetersPerSecond;
    
    private DriveVelCommandPacket(byte[] packetData) {
        if (packetData.length != 5) {
            throw new IllegalArgumentException();
        }
        
        this.commandId = packetData[0] & 0xFF;
        this.leftVelocityMetersPerSecond = parseSingleVelocity(packetData[1], packetData[2]);
        this.rightVelocityMetersPerSecond = parseSingleVelocity(packetData[3], packetData[4]);
    }
    
    public static double parseSingleVelocity(byte firstByte, byte secondByte) {
        int total = (firstByte << 8) | (secondByte & 0xFF);
        return total / 100d;
    }
    
    public static DriveVelCommandPacket parse(byte[] packetData) {
        return new DriveVelCommandPacket(packetData);
    }
}
