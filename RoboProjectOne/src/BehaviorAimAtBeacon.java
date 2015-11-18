import lejos.robotics.subsumption.Behavior;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.*;
import lejos.utility.Delay;


public class BehaviorAimAtBeacon implements Behavior {
	// used for detecting the edge
	private SharedIRSensor ir;
	// used for stopping and rotating the robot
	private SharedDifferentialPilot sharedPilot;
	public boolean looking = true;//looking for or traveling to the beacon
	
	public BehaviorAimAtBeacon(SharedDifferentialPilot sharedPilot, SharedIRSensor ir){
		this.ir = ir;
		ir.setSeek();
		this.sharedPilot = sharedPilot;
	}
	
	
	@Override
	public boolean takeControl() {
		ir.tmpSeek();
		return ((ir.bearing > 5 || ir.bearing < -5) && StateManager.getInstance().getState() == 0);
	}

	@Override
	public void action() {
		ir.setSeek();
		while(ir.bearing > 5 || ir.bearing < -5){//turn toward beacon
			if(ir.bearing > 5)//keep beacon between 5...
			{
				sharedPilot.robot.rotate(-2);				
			}
			else if(ir.distance > 10  && ir.bearing < -5)//and -5
			{
				sharedPilot.robot.rotate(2);
			}

		}//end while looking
	}

	@Override	
	public void suppress() {}
}

