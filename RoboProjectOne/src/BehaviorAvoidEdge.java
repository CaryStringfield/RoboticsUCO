import lejos.robotics.subsumption.Behavior;

public class BehaviorAvoidEdge implements Behavior {
	// used for detecting the edge
	private SharedUltraSonicSensor leftUSS;
	private SharedUltraSonicSensor rightUSS;
	// used for stopping and rotating the robot
	private SharedDifferentialPilot sharedPilot;

	// the allowed limit to be read from the UltraSonic sensor
	private float edgeThreshold = 0.07f;
	// the distance to back up
	private int backoffDistance = -8;
	// store distance reading for each side
		
	public BehaviorAvoidEdge(SharedDifferentialPilot sharedPilot, SharedUltraSonicSensor leftUSS, SharedUltraSonicSensor rightUSS){
		this.leftUSS =leftUSS;
		this.rightUSS =rightUSS;
		this.sharedPilot = sharedPilot;
	}
	
	
	@Override
	public boolean takeControl() {
		// take control if the distance on the both UltraSomic sensors are too high
		return (leftUSS.distance>edgeThreshold && rightUSS.distance>edgeThreshold);
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
