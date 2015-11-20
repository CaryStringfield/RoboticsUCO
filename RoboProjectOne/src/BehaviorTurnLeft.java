import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

/*
 * This class was a test to see how a turning thread would work. The implementation of this
 * class was grouped with BehaviorTurnRight into BehaviorSteer. This was done because of 
 * issues with TurnLeft and TurnRight fighting for control of the motors.
 */
public class BehaviorTurnLeft implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedColorSensor sharedColor;
	boolean turn_left = false ;
	//the amount to turn
	private int correction = 20;

	//constructor to initialize the sensor and motor
	public  BehaviorTurnLeft(SharedDifferentialPilot pilot, SharedColorSensor clrs) {
		this.sharedPilot = pilot;
		this.sharedColor = clrs;
	}

	@Override
	public boolean takeControl() {
		// dist low means that sensor is farther off table, needs to turn away
		return false;//(sharedColor.distLow && !sharedColor.distHigh);		
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
			Thread.yield();
		}
	}

	@Override
	public void suppress() {

		LCD.drawString("LEFT suppress", 0, 5); //for debugging later
		turn_left = false;
		sharedPilot.robot.stop();
	}
}
