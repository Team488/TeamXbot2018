package competition.subsystems.offboard;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.offboard.packets.DrivePowerCommandPacket;

public class OffboardDriveCommandParserTest extends BaseCompetitionTest {
    
    @Test
    public void testSimple() {
        DrivePowerCommandPacket packet = DrivePowerCommandPacket.parse(new byte[] {
            0x01,
            (byte)0b00000001, (byte)0b00101100,
            (byte)0b11111110, (byte)0b11010100
        });

        assertEquals(1, packet.commandId);
        assertEquals(0.1,  packet.leftPower, 1e-5);
        assertEquals(-0.1,  packet.rightPower, 1e-5);
    }
}
