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
//		this.seek = ir.getMode("Seek");
		vals = new float [8];
		
	}
	
	
	@Override
	public boolean takeControl() {
		//set to seek mode
		this.ir.setSeek();
		
		// take control
		return (looking);
	}

	@Override
	public void action() {
		// look for beacon
		Delay.msDelay(50);
		
		seek.fetchSample(vals,0); //this may break here
		bearing = (int)vals[0];
		range = (int)vals[1];
		
		while(looking){
			if(range == 0 &&(bearing == 128 || bearing == 0))//beacon not visible
			{
				sharedPilot.robot.rotate(10);	//rotate till you see the beacon			
			}
			else if(range > 10  && bearing < -5)//keep beacon between -5...
			{
				sharedPilot.robot.rotate(5);
			}
			else if(range > 10  && bearing > 5)//and 5 degrees
			{
				sharedPilot.robot.rotate(5);
			}
			else if(range < 10)//stop 10 away from beacon
			{
				sharedPilot.robot.stop();
				looking = false;
			}

		}//end while looking
	}

	@Override	
	public void suppress() {}
}

