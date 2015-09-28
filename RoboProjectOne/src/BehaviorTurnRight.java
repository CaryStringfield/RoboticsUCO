import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

public class BehaviorTurnRight implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedColorSensor sharedColor;
	boolean turn_right = false ;

	public  BehaviorTurnRight(SharedDifferentialPilot pilot, SharedColorSensor clrs) {
		this.sharedPilot = pilot;
		this.sharedColor = clrs;
	}

	@Override
	public boolean takeControl() {
		return (sharedColor.distLow == true);		
	}	

	@Override
	public void action() {
		turn_right = true;
		
		//this could work for a forward moving turning mechanic
		sharedPilot.robot.steer(-20);

		turn_right = false;
	}

	@Override
	public void suppress() {
		while(turn_right)
			Thread.yield();
	}
}
