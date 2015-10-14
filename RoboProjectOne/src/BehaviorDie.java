import lejos.hardware.Button;
import lejos.robotics.subsumption.*;

public class BehaviorDie implements Behavior{

	@Override
	public boolean takeControl() {
		// the escape button on the robot is pressed
		return Button.ESCAPE.isDown();
	}

	@Override
	public void action() {
		// inform the user the robot is stopping
		System.out.println("Stopping...");
		// wait for another button press
		Button.waitForAnyPress();
		// shutdown the program
		System.exit(1);
	}

	@Override
	public void suppress() {
	}

}
