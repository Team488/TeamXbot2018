package competition.subsystems.autonomous.selection;

import java.util.function.Supplier;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.wpi.first.wpilibj.command.Command;
import xbot.common.command.BaseSubsystem;
import xbot.common.properties.StringProperty;
import xbot.common.properties.XPropertyManager;

@Singleton
public class AutonomousCommandSelector extends BaseSubsystem {
    private static Logger log = Logger.getLogger(AutonomousCommandSelector.class);

    public final StringProperty currentAutonomousCommandName;
    Supplier<Command> commandSupplier;

    Command currentAutonomousCommand;

    @Inject
    public AutonomousCommandSelector(XPropertyManager propManager) {
        currentAutonomousCommandName = propManager.createEphemeralProperty(getPrefix() + "Current autonomous command name",
                "No command set");
    }

    public Command getCurrentAutonomousCommand() {
        if (commandSupplier != null) {
            return commandSupplier.get();
        }
        return currentAutonomousCommand;
    }

    public void setCurrentAutonomousCommand(Command currentAutonomousCommand) {
        log.info("Setting CurrentAutonomousCommand to " + currentAutonomousCommand);
        this.currentAutonomousCommandName
                .set(currentAutonomousCommand == null ? "No command set" : currentAutonomousCommand.toString());

        this.currentAutonomousCommand = currentAutonomousCommand;
        commandSupplier = null;
    }
    
    public void setCurrentAutonomousCommandSupplier(Supplier<Command> supplier) {
        commandSupplier = supplier;
        this.currentAutonomousCommand = null;
    }

}
