import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

public class BehaviorTurnLeft implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedColorSensor sharedColor;
	boolean turn_left = false ;
	private int correction = 20;

	public  BehaviorTurnLeft(SharedDifferentialPilot pilot, SharedColorSensor clrs) {
		this.sharedPilot = pilot;
		this.sharedColor = clrs;
	}

	@Override
	public boolean takeControl() {
		// dist low means that sensor is farther off table, needs to turn away
		return (sharedColor.distLow == true);		
	}	

	@Override
	public void action() {
		turn_left = true;
		
		boolean temp = false;
		while (turn_left){
			//this could work for a forward moving turning mechanic
			if (!temp){
				sharedPilot.robot.steer(correction);
				temp = true;
			}
		}
	}

	@Override
	public void suppress() {
		turn_left = false;
	}
}
