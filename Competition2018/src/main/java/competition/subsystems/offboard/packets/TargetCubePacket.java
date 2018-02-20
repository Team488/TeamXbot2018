package competition.subsystems.offboard.packets;

import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.data.TargetCubeInfo;

public class TargetCubePacket {
    public final TargetCubeInfo targetInfo;
    
    private TargetCubePacket(byte[] packetData) {
        if (packetData.length != 7) {
            throw new IllegalArgumentException();
        }
        
        boolean hasTarget = packetData[0] != 0;
        
        if(hasTarget) {
            double rosXMeters = parseSingleDim(packetData[1], packetData[2]);
            double rosYMeters = parseSingleDim(packetData[3], packetData[4]);
            double rosZMeters = parseSingleDim(packetData[5], packetData[6]);
            this.targetInfo = new TargetCubeInfo(
                    -yMeters / OffboardInterfaceSubsystem.METERS_PER_INCH,
                    xMeters / OffboardInterfaceSubsystem.METERS_PER_INCH,
                    zMeters / OffboardInterfaceSubsystem.METERS_PER_INCH);
        }
        else {
            this.targetInfo = null;
        }
    }
    
    public static double parseSingleDim(byte firstByte, byte secondByte) {
        int total = (firstByte << 8) | (secondByte & 0xFF);
        return total / 300d;
    }
    
    public static TargetCubePacket parse(byte[] packetData) {
        return new TargetCubePacket(packetData);
    }
}
