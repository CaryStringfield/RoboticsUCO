import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

//behavior was an example in the book and is not currently implemented
//==================================================================== 
//stop if the robot is within 40 cm of an object and move about
	public class BehaviorProximity implements Behavior {
		RegulatedMotor left;
		RegulatedMotor right;
		SharedIRSensor ir;
		boolean backing_up = false;
		
		//this initialized the motors and IR sensor
		public  BehaviorProximity(RegulatedMotor left, RegulatedMotor right, SharedIRSensor ir) {
			this.left = left;
			this.right = right;
			this.ir = ir;
		}
		
		//if the distance is below 40 cm then the thread takes control
		public boolean takeControl() {
			ir.tmpDist();
			return (ir.distance < 40);
		}
		
		// The behavior originally backed up, we played with the values a bit. 
		// These may not be the orginal behavior values.
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
	