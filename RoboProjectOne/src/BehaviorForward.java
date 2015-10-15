import lejos.robotics.subsumption.Behavior;

//move the robot forward	
public class BehaviorForward implements Behavior {
	private SharedDifferentialPilot sharedPilot;
	public boolean moving = false;
		
	public BehaviorForward(SharedDifferentialPilot sharedPilot) {
		this.sharedPilot = sharedPilot;
	}
		// behavior should take control if no other behavior is triggered
	public boolean takeControl() {
		return true;
	}
		
	public void action() {
		moving = true;
		
		// used so robot.forward is only called once
		// if forward is called multiple times the robot staggers
		boolean temp = false;
		while(moving){
			if (!temp){
				// move forward
				sharedPilot.robot.forward();
				temp = true;
			}
			Thread.yield();
		}
	}
		 
	public void suppress() {
		// escape the loop
		moving = false;
		// stop moving
		sharedPilot.robot.stop();
	}
}	
	
