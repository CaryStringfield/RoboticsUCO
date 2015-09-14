import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;

//move the robot forward	
	public class BehaviorForward implements Behavior {
		RegulatedMotor left;
		RegulatedMotor right;
		
		public BehaviorForward(RegulatedMotor left, RegulatedMotor right) {
			this.left = left;
			this.right = right;
		}
		
		public boolean takeControl() {
			return true;
		}
		
		public void action() {
			left.backward();
			right.backward();
		}
		
		public void suppress() {}
	}
	
	
