package competition.subsystems.elevator.commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import competition.BaseCompetitionTest;
import competition.subsystems.elevator.ElevatorSubsystem;
import edu.wpi.first.wpilibj.MockXboxControllerAdapter;	
import xbot.common.controls.actuators.mock_adapters.MockCANTalon;
import xbot.common.injection.BaseWPITest;

public class MoveToMaxHeightCommandTest extends BaseCompetitionTest {

	MoveToMaxHeightCommand command;
	ElevatorSubsystem elevator;
	
	@Override
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		
		command = injector.getInstance(MoveToMaxHeightCommand.class);
		elevator = injector.getInstance(ElevatorSubsystem.class);
		elevator.temporaryHack();
	}
	
	@Test
	public void testSimple() {
		command.initialize();
		command.execute();
	}
	
	@Test
	public void verifyMovingUp() {
		
		command.initialize();
		command.execute();
		
		assertTrue(elevator.motor.getMotorOutputPercent() >= 0.1);
	}
}
