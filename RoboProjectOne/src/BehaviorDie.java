import lejos.hardware.Button;
import lejos.robotics.subsumption.*;

public class BehaviorDie implements Behavior{

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Button.ESCAPE.isDown();
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println("Stopping...");
		Button.waitForAnyPress();
		System.exit(1);
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
