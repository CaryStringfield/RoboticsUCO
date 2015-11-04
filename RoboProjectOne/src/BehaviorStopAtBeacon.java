import lejos.robotics.subsumption.Behavior;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.*;
import lejos.utility.Delay;


public class BehaviorStopAtBeacon implements Behavior {
	// used for detecting the edge
	private SharedIRSensor ir;
	// used for stopping and rotating the robot
	private SharedDifferentialPilot sharedPilot;
	public boolean looking = true;//looking for or traveling to the beacon

	
	public BehaviorStopAtBeacon(SharedDifferentialPilot sharedPilot, SharedIRSensor ir){
		this.ir = ir;
		this.sharedPilot = sharedPilot;
		
	}
	
	
	@Override
	public boolean takeControl() {
		// take control
		ir.tmpSeek();
		return (ir.distance < 10 && (ir.bearing < 5 && ir.bearing > -5));
	}

	@Override
	public void action() {
		// look for beacon
		//Delay.msDelay(50);
		sharedPilot.robot.stop();
	}

	@Override	
	public void suppress() {}
}

