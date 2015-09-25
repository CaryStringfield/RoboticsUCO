import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;

//move the robot forward	
	public class BehaviorForward implements Behavior {
		private SharedDifferentialPilot sharedPilot;
		
		public BehaviorForward(SharedDifferentialPilot sharedPilot) {
			this.sharedPilot = sharedPilot;
		}
		
		public boolean takeControl() {
			return true;
		}
		
		public void action() {
			sharedPilot.robot.forward();
		}
		 
		public void suppress() {
			sharedPilot.robot.stop();
		}
	}
	
	
