import lejos.robotics.subsumption.Behavior;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.*;
import lejos.utility.Delay;


public class BehaviorStopAtBeacon implements Behavior {
	// used for detecting the edge
	private SharedIRSensor ir;
	//grabber
	private SharedGrabber grabber;
	// used for stopping and rotating the robot
	private SharedDifferentialPilot sharedPilot;
	public boolean looking = true;//looking for or traveling to the beacon

	
	public BehaviorStopAtBeacon(SharedDifferentialPilot sharedPilot, SharedIRSensor ir, SharedGrabber grab){
		this.ir = ir;
		this.sharedPilot = sharedPilot;
		this.grabber = grab;
	}
	
	
	@Override
	public boolean takeControl() {
		// take control
		ir.tmpSeek();
		return ((ir.distanceSeek < 10 && (ir.bearing < 5 && ir.bearing > -5)) && StateManager.getInstance().getState() == 0);
	}

	@Override
	public void action() {
		// look for beacon
		//Delay.msDelay(50);
		sharedPilot.robot.stop();
		grabber.openClaw();
		while(grabber.state=="opening");
		
		sharedPilot.robot.travel(-5);
		sharedPilot.robot.rotate(180);
		
		StateManager.getInstance().setState(1);
	}

	@Override	
	public void suppress() {}
}

