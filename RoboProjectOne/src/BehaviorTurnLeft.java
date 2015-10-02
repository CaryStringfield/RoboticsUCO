import lejos.hardware.lcd.LCD;
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
		return (sharedColor.distLow && !sharedColor.distHigh);		
	}	

	@Override
	public void action() {
		turn_left = true;
		
		boolean temp = false;
		while (turn_left && sharedColor.distLow){
			//this could work for a forward moving turning mechanic
			if (!temp){
				sharedPilot.robot.steer(correction);
				temp = true;
			}
		}
	}

	@Override
	public void suppress() {

		LCD.drawString("LEFT suppress", 0, 5); //for debugging later
		turn_left = false;
		//sharedPilot.robot.steer(0);
		sharedPilot.robot.stop();
	}
}
