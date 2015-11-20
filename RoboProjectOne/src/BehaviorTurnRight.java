import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

/*
 * This class was a test to see how a turning thread would work. The implementation of this
 * class was grouped with BehaviorTurnLeft into BehaviorSteer. This was done because of 
 * issues with TurnLeft and TurnRight fighting for control of the motors.
 */
public class BehaviorTurnRight implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedColorSensor sharedColor;
	private boolean turn_right = false ;
	//this value told the motor which way to steer
	private int correction = -20;

	//constructor to initialize the motors and sensor
	public  BehaviorTurnRight(SharedDifferentialPilot pilot, SharedColorSensor clrs) {
		this.sharedPilot = pilot;
		this.sharedColor = clrs;
	}

	@Override
	public boolean takeControl() {
		// distHigh means sensor is more on the table, so need to turn toward edge
		return false;//(sharedColor.distHigh && !sharedColor.distLow);		
	}	

	@Override
	public void action() {
		turn_right = true;
		
		boolean temp = false;
		while (turn_right){
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

		LCD.drawString("RIGHT suppress", 0, 5); //for debugging later
		turn_right = false;
		sharedPilot.robot.stop();
	}
}
