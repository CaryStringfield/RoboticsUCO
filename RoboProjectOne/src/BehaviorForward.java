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
			//if (moving == false){
			boolean temp = false;
			while(moving){
				if (temp == false){
					sharedPilot.robot.forward();
					temp = true;
				}
				//moving = true;
			}
		}
		 
		public void suppress() {
			moving = false;
			sharedPilot.robot.stop();
		}
	}
	
	
