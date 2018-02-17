package competition.subsystems.offboard.packets;

import org.apache.log4j.Logger;

public class ElevatorPositionCommandPacket {
	
	private static Logger log = Logger.getLogger(ElevatorPositionCommandPacket.class);
	
	public final int commandId;
	public final ElevatorGoal elevatorGoal;
	
	public enum ElevatorGoal {
		PowerCubeGround, Switch, ScaleLow, ScaleMid, ScaleHigh
	}
	
	public ElevatorPositionCommandPacket(byte[] packetData) {
		this.commandId = packetData[0] & 0xFF;
		
		switch (packetData[1]) {
			case 1: this.elevatorGoal = ElevatorGoal.PowerCubeGround;
				break;
			case 2: this.elevatorGoal = ElevatorGoal.Switch;
				break;
			case 3: this.elevatorGoal = ElevatorGoal.ScaleLow;
				break;
			case 4: this.elevatorGoal = ElevatorGoal.ScaleMid;
				break;
			case 5: this.elevatorGoal = ElevatorGoal.ScaleHigh;
				break;
			default: this.elevatorGoal = null;
				log.error("Elevator goal: " + packetData[1] + " is invalid");
				break;
		}
	}

	public static ElevatorPositionCommandPacket parse(byte[] packetData) {
		return new ElevatorPositionCommandPacket(packetData);
	}
}
