import lejos.robotics.subsumption.*;

public class BehaviorSteerColor implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedColorSensor sharedColor;
	private boolean turning = false;
	// the amount to steer to or from the edge
	private int correction = 20;
	// the number of times to perform the edge alignment movements
	private int maxrot = 3;

	//constructor to initialize sensor and motor
	public  BehaviorSteerColor(SharedDifferentialPilot pilot, SharedColorSensor clrs) {
		this.sharedPilot = pilot;
		this.sharedColor = clrs;
	}

	@Override
	public boolean takeControl() {
		// distHigh means sensor is more on the table, so need to turn toward edge
		// distLow means sensor is more off the table, so need to turn away from edge
		// take control when either it true
		return false;//(sharedColor.distHigh || sharedColor.distLow);		
	}	
	
	@Override
	public void action() {
		turning = true;
		
		// this variable is used so steer is only called on
		// if steer is called more than once it causes the robot to stagger
		boolean temp = false;
		// this marks the current direction of the robot
		// i.e. away from or toward the edge (true or false respectively)
		boolean isLeft = true;
		// continue to correct course until suppressed
		while (turning){
			
			if (!temp){
				// stop the robot before changing direction
				sharedPilot.robot.stop();
				// if color sensor reads high, steer toward the edge
				//if (sharedColor.distHigh)
					sharedPilot.robot.steer(-correction);
				// the color sensor is reading low
				//else if(sharedColor.distLow) {
					// if the alignment steps have not been taking
					if (maxrot > 0){
						// stop moving
						sharedPilot.robot.stop();
						// back away from the edge
						sharedPilot.robot.travel(-5);
						// make a small rotation so the robot aligns more
						// closely with the edge of the table
						sharedPilot.robot.rotate(15);
						// continue driving
						sharedPilot.robot.forward();
						maxrot--;
					}
					// else continue normal operation and steer away from the edge
					else {
						sharedPilot.robot.steer(correction);
					}
						
				//}
				temp = true;
			}
			
			// if the sensor says the robot is moving one way
			// but isLeft says something else, then a course change is required
			//if (sharedColor.distHigh && isLeft){
				temp = false;
				isLeft = false;
			//}else if (sharedColor.distLow && !isLeft){
				temp = false;
				isLeft = true;
			//}
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		// escape the while loop
		turning = false;
		// stop moving
		sharedPilot.robot.stop();
	}
}
