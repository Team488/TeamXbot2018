package competition.subsystems.offboard.packets;

public class DroneMotorCommandPacket {
    public final int leftFront;
    public final int leftRear;
    public final int rightFront;
    public final int rightRear;
    
    private DroneMotorCommandPacket(byte[] packetData) {
        if (packetData.length != 8) {
            throw new IllegalArgumentException();
        }
        
        this.leftFront = parseSinglePulseWidth(packetData[0], packetData[1]);
        this.leftRear = parseSinglePulseWidth(packetData[2], packetData[3]);
        this.rightFront = parseSinglePulseWidth(packetData[4], packetData[5]);
        this.rightRear = parseSinglePulseWidth(packetData[6], packetData[7]);
    }
    
    public static int parseSinglePulseWidth(byte firstByte, byte secondByte) {
        int total = (firstByte << 8) | (secondByte & 0xFF);
        return total;
    }
    
    public static DroneMotorCommandPacket parse(byte[] packetData) {
        return new DroneMotorCommandPacket(packetData);
    }
}
