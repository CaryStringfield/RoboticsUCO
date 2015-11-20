import lejos.robotics.subsumption.*;

public class BehaviorSteer implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedUltraSonicSensor leftUSS;
	private SharedUltraSonicSensor rightUSS;

	// the amount to steer to or from the edge
	private int correction = 30;
	// the allowed limit to be read from the UltraSonic sensor
	private float edgeThreshold = 0.07f;

	//constructor to initialize sensor and motor
	public  BehaviorSteer(SharedDifferentialPilot pilot, SharedUltraSonicSensor leftUSS, SharedUltraSonicSensor rightUSS) {
		this.sharedPilot = pilot;
		this.leftUSS =leftUSS;
		this.rightUSS =rightUSS;
	}

	@Override
	public boolean takeControl() {
	     // the sensor is off the table, so need to turn away from edge
		return (leftUSS.distance>edgeThreshold || rightUSS.distance>edgeThreshold);
	}	
	
	@Override
	public void action() {
				
		if(rightUSS.distance>edgeThreshold)
		{
			
			// stop moving
			sharedPilot.robot.stop();
			// back away from the edge
			sharedPilot.robot.travel(-5);
			// make a rotation to move away from the edge of the table
			sharedPilot.robot.rotate(correction);
			// move forward a little
			sharedPilot.robot.travel(5);
			// continue driving
		
		}
		else if(leftUSS.distance>edgeThreshold)
		{		
			// stop moving
			sharedPilot.robot.stop();
			// back away from the edge
			sharedPilot.robot.travel(-5);
			// make a rotation to move away from the edge of the table
			sharedPilot.robot.rotate(-correction);
			// back away from the edge
			sharedPilot.robot.travel(5);
			// continue driving
			
		}
		
		Thread.yield();
			
	}
	

	@Override
	public void suppress() {
				// stop moving
		sharedPilot.robot.stop();
	}
}
