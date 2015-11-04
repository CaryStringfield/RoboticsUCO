import lejos.robotics.subsumption.Behavior;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.*;
import lejos.utility.Delay;


public class BehaviorSeek implements Behavior {
	// used for detecting the edge
	private SharedIRSensor ir;
	// used for stopping and rotating the robot
	private SharedDifferentialPilot sharedPilot;
	public boolean looking = true;//looking for or traveling to the beacon
	private SensorMode seek;
	private float [] vals;
	private int range;
	private int bearing;

	
	public BehaviorSeek(SharedDifferentialPilot sharedPilot, SharedIRSensor ir){
		this.ir = ir;
		this.sharedPilot = sharedPilot;
		this.seek = ir.getMode("Seek");
		vals = new float [8];
		
	}
	
	
	@Override
	public boolean takeControl() {
		//set to seek mode
		//this.ir.setSeek();
		//this.seek = ir.getMode("Seek");

		// take control
		seek.fetchSample(vals,0); //this may break here
		bearing = (int)vals[0];
		range = (int)vals[1];
		return (bearing > 5 || bearing < -5)
	}

	@Override
	public void action() {
		// look for beacon
		Delay.msDelay(50);
		
		
		while(bearing > 5 || bearing < -5){//turn toward beacon
			if(bearing > 5)//keep beacon between 5...
			{
				sharedPilot.robot.rotate(-2);				
			}
			else if(range > 10  && bearing < -5)//and -5
			{
				sharedPilot.robot.rotate(2);
			}

		}//end while looking
	}

	@Override	
	public void suppress() {}
}

