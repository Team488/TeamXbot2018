package competition.subsystems.offboard.packets;

public class DroneMotorCommandPacket {
    public final double leftFront;
    public final double leftRear;
    public final double rightFront;
    public final double rightRear;
    
    private DroneMotorCommandPacket(byte[] packetData) {
        if (packetData.length != 8) {
            throw new IllegalArgumentException();
        }
        
        this.leftFront = parseSinglePower(packetData[0], packetData[1]);
        this.leftRear = parseSinglePower(packetData[2], packetData[3]);
        this.rightFront = parseSinglePower(packetData[4], packetData[5]);
        this.rightRear = parseSinglePower(packetData[6], packetData[7]);
    }
    
    public static double parseSinglePower(byte firstByte, byte secondByte) {
        int total = (firstByte << 8) | (secondByte & 0xFF);
        return total / 100d;
    }
    
    public static DroneMotorCommandPacket parse(byte[] packetData) {
        return new DroneMotorCommandPacket(packetData);
    }
}
