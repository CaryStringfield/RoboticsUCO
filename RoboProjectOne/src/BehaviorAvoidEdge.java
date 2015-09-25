import lejos.robotics.subsumption.Behavior;

public class BehaviorAvoidEdge implements Behavior {
	private SharedIRSensor ir;
	private SharedDifferentialPilot sharedPilot;
	
	private int edgeThreshold = 3;
	private int backoffDistance = -20;
	
	public BehaviorAvoidEdge(SharedDifferentialPilot sharedPilot, SharedIRSensor ir){
		this.ir = ir;
		this.sharedPilot = sharedPilot;
	}
	
	
	@Override
	public boolean takeControl() {
		return (ir.distance>edgeThreshold);
	}

	@Override
	public void action() {
		sharedPilot.robot.stop();
		sharedPilot.robot.travel(backoffDistance);
		sharedPilot.robot.rotate(90);
	}

	@Override
	public void suppress() {}

}
