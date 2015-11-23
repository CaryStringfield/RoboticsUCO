import lejos.robotics.subsumption.Behavior;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.*;
import lejos.utility.Delay;


public class BehaviorSeek implements Behavior {
	// used for detecting the edge
	private SharedIRSensor ir;
	// used for stopping and rotating the robot
	private SharedDifferentialPilot sharedPilot;
	private int numDegrees = 5;
	private int maxRot = 270;

	
	public BehaviorSeek(SharedDifferentialPilot sharedPilot, SharedIRSensor ir){
		this.ir = ir;
		this.sharedPilot = sharedPilot;
	}
	
	
	@Override
	public boolean takeControl() {
		return (StateManager.getInstance().getState()==2);
	}

	@Override
	public void action() {
		ir.setDistance();
		
		int numrots = 270;
		while(!(ir.distance>0 && ir.distance<60) && numrots>0){
			numrots -= numDegrees;
			sharedPilot.robot.rotate(numDegrees);
		}
		
		StateManager.getInstance().setState(1);
	}

	@Override	
	public void suppress() {}
}

