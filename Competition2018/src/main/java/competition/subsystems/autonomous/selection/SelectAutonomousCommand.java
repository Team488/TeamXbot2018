package competition.subsystems.autonomous.selection;

import com.google.inject.Inject;

import competition.subsystems.autonomous.AutonomousCommandSupplier;
import competition.subsystems.autonomous.AutonomousCommandSupplier.AutonomousMetaprogram;
import xbot.common.command.BaseCommand;

public class SelectAutonomousCommand extends BaseCommand {

    AutonomousCommandSupplier autoCommandSupplier;
    AutonomousCommandSelector autoSelector;
    AutonomousMetaprogram metaprogram;
    
    @Inject
    public SelectAutonomousCommand(AutonomousCommandSelector autoSelector, AutonomousCommandSupplier autoCommandSupplier) {
        this.autoSelector = autoSelector;
        this.autoCommandSupplier = autoCommandSupplier;
        this.setRunWhenDisabled(true);
    }
    
    public void setMetaprogram(AutonomousMetaprogram metaprogram) {
        this.metaprogram = metaprogram;
    }

    @Override
    public void initialize() {
        log.info("Initializing with goal feature: " + metaprogram.toString());
        autoCommandSupplier.setMetaprogram(metaprogram);
        autoSelector.setCurrentAutonomousCommandSupplier(autoCommandSupplier.getAutoSupplier());
    }

    @Override
    public void execute() {
    }    
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
