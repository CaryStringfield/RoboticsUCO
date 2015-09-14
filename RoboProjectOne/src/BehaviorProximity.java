import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

//stop if the robot is within 40 cm of an object
	public class BehaviorProximity implements Behavior {
		RegulatedMotor left;
		RegulatedMotor right;
		SharedIRSensor ir;
		boolean backing_up = false;
		
		public  BehaviorProximity(RegulatedMotor left, RegulatedMotor right, SharedIRSensor ir) {
			this.left = left;
			this.right = right;
			this.ir = ir;
		}
		
		public boolean takeControl() {
			return (ir.distance < 40);
		}
		
		public void action() {
			backing_up = true;
			
			left.rotate(600, true);
			right.rotate(600);
			
			left.rotate(-450, true);
			left.rotate(450);
			
			backing_up = false;
		}
		
		public void suppress() {
			//wait until backup done
			while (backing_up) {
				Thread.yield();
			}
		}
	}
	