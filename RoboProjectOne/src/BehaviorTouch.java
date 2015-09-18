import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;

//try to turn back onto surface if edge is detected
public class BehaviorTouch implements Behavior {
	RegulatedMotor left;
	RegulatedMotor right;
	SharedTouchSensor touch;
	boolean turn_left = false;
	
	public BehaviorTouch(RegulatedMotor left, RegulatedMotor right, SharedTouchSensor tch) {
		this.left = left;
		this.right = right;
		this.touch = tch;
	}
	
	@Override
	public boolean takeControl() {
		return (touch.contact == false);
	}

	@Override
	public void action() {
		turn_left = true;
		
		left.flt();

		turn_left = false;
	}

	@Override
	public void suppress() {
		while(turn_left)
			Thread.yield();
	}

}
