package competition.subsystems.offboard.commands;

import competition.subsystems.offboard.OffboardCommsConstants;
import competition.subsystems.offboard.OffboardCommunicationPacket;
import competition.subsystems.offboard.OffboardInterfaceSubsystem;
import competition.subsystems.offboard.packets.CommandFinishedPacket;
import xbot.common.command.BaseCommand;

public abstract class OffboardProcessingCommand extends BaseCommand {
    protected final int commandId;
    protected final OffboardInterfaceSubsystem subsystem;
    
    private boolean isRemoteFinished = false;
    
    protected OffboardProcessingCommand(int commandId, OffboardInterfaceSubsystem subsystem) {
        if(commandId <= 0 || commandId > 255) {
            throw new IllegalArgumentException("Command ID must be a valid nonzero unsigned byte value");
        }
        
        this.commandId = commandId;
        this.subsystem = subsystem;
        
        this.requires(subsystem);
    }
    
    private String stringifyCommandId() {
        return "0x" + Integer.toHexString(commandId).toUpperCase();
    }
    
    @Override
    public void initialize() {
        log.info("Sending remote start for command ID " + stringifyCommandId());
        subsystem.sendSetCurrentCommand(commandId);
    }
    
    protected abstract void handleIncomingPacket(OffboardCommunicationPacket packet);
    
    @Override
    public final void execute() {
        // TODO: Tune loop? Logging when hit limit?
        for(int receiveCount = 0; receiveCount < 5; receiveCount++) {
            OffboardCommunicationPacket packet = subsystem.rawCommsInterface.receiveRaw();
            if(packet == null) {
                break;
            }
            
            if(packet.packetType == OffboardCommsConstants.PACKET_TYPE_COMMAND_FINISHED) {
                handleCommandFinishedPacket(CommandFinishedPacket.parse(packet.data));
            }
            else {
                handleIncomingPacket(packet);
            }
        }
    }
    
    private void handleCommandFinishedPacket(CommandFinishedPacket finishedPacket) {
        if (finishedPacket.commandId == this.commandId) {
            log.info("Command " + stringifyCommandId() + " terminated with status code " + finishedPacket.statusCode);
            this.isRemoteFinished = true;
            return;
        }
        else {
            log.warn("Received \"command finished\" packet for command which is not currently running!");
        }
    }
    
    @Override
    public boolean isFinished() {
        return isRemoteFinished;
    }
    
    @Override
    public void interrupted() {
        log.info("Interrupted; stopping remote command");
        subsystem.sendSetCurrentCommand(0);
    }
}
