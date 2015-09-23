import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

public class BehaviorSenseEdge implements Behavior {
	
	RegulatedMotor left;
	RegulatedMotor right;
	SharedColorSensor clr;
	boolean turn_left = false;

	public  BehaviorSenseEdge(RegulatedMotor left, RegulatedMotor right, SharedColorSensor clrs) {
		this.left = left;
		this.right = right;
		this.clr = clrs;
	}

	@Override
	public boolean takeControl() {
		return (clr.edge == true);		
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
