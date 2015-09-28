import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

public class BehaviorTurnLeft implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedColorSensor sharedColor;
	boolean turn_left = false ;

	public  BehaviorTurnLeft(SharedDifferentialPilot pilot, SharedColorSensor clrs) {
		this.sharedPilot = pilot;
		this.sharedColor = clrs;
	}

	@Override
	public boolean takeControl() {
		return (sharedColor.distHigh == true);		
	}	

	@Override
	public void action() {
		turn_left = true;
		
		//this could work for a forward moving turning mechanic
		sharedPilot.robot.steer(20);

		turn_left = false;
	}

	@Override
	public void suppress() {
		while(turn_left)
			Thread.yield();
	}
}
