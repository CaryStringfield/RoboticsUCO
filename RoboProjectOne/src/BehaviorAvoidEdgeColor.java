import lejos.robotics.subsumption.Behavior;

public class BehaviorAvoidEdgeColor implements Behavior {
	// used for detecting the edge
	private SharedIRSensor ir;
	// used for stopping and rotating the robot
	private SharedDifferentialPilot sharedPilot;
	
	// the allowed limit to be read from the ir sensor
	private int edgeThreshold = 5;
	// the distance to back up
	private int backoffDistance = -8;
	
	public BehaviorAvoidEdgeColor(SharedDifferentialPilot sharedPilot, SharedIRSensor ir){
		this.ir = ir;
		this.sharedPilot = sharedPilot;
	}
	
	
	@Override
	public boolean takeControl() {
		//set to distance mode
		this.ir.tmpDist();
		
		// take control if the distance on the IR sensor is too high
		return (ir.distance>edgeThreshold);
	}

	@Override
	public void action() {
		// stop moving, back up, turn 90 degrees
		sharedPilot.robot.stop();
		sharedPilot.robot.travel(backoffDistance);
		sharedPilot.robot.rotate(90);
		sharedPilot.robot.stop();
	}

	@Override	
	public void suppress() {}
}
