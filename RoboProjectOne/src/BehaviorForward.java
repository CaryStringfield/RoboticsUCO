import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;

//move the robot forward	
	public class BehaviorForward implements Behavior {
		private SharedDifferentialPilot sharedPilot;
		public boolean moving = false;
		
		public BehaviorForward(SharedDifferentialPilot sharedPilot) {
			this.sharedPilot = sharedPilot;
		}
		
		public boolean takeControl() {
			return true;
		}
		
		public void action() {
			moving = true;
			
			boolean temp = false;
			while(moving){
				if (!temp){
					sharedPilot.robot.forward();
					temp = true;
				}
				Thread.yield();
			}
		}
		 
		public void suppress() {
			moving = false;
			sharedPilot.robot.stop();
		}
	}
	
	
