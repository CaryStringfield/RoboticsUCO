import lejos.robotics.subsumption.*;

public class BehaviorSteer implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedUltraSonicSensor leftUSS;
	private SharedUltraSonicSensor rightUSS;

	// the amount to steer to or from the edge
	private int correction = 30;
	// the allowed limit to be read from the UltraSonic sensor
	private float edgeThreshold = 0.06f;

	//constructor to initialize sensor and motor
	public  BehaviorSteer(SharedDifferentialPilot pilot, SharedUltraSonicSensor leftUSS, SharedUltraSonicSensor rightUSS) {
		this.sharedPilot = pilot;
		this.leftUSS =leftUSSr;
		this.rightUSS =rightUSSr;
	}

	@Override
	public boolean takeControl() {
	     // the sensor is off the table, so need to turn away from edge
		return (leftUSS.getDistance()>edgeThreshold || righttUSS.getDistance()>edgeThreshold);
	}	
	
	@Override
	public void action() {
				
		if(rightUSS.getDistance>edgeThreshold)
		{
			
			// stop moving
			sharedPilot.robot.stop();
			// back away from the edge
			sharedPilot.robot.travel(-5);
			// make a rotation to move away from the edge of the table
			sharedPilot.robot.rotate(correction);
			// continue driving
		
		}
		else if(leftUSS.getDistance>edgeThreshold)
		{		
			// stop moving
			sharedPilot.robot.stop();
			// back away from the edge
			sharedPilot.robot.travel(-5);
			// make a rotation to move away from the edge of the table
			sharedPilot.robot.rotate(-correction);
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
