import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

public class BehaviorSteer implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedColorSensor sharedColor;
	private boolean turning = false ;
	private int correction = 20;

	public  BehaviorSteer(SharedDifferentialPilot pilot, SharedColorSensor clrs) {
		this.sharedPilot = pilot;
		this.sharedColor = clrs;
	}

	@Override
	public boolean takeControl() {
		// distHigh means sensor is more on the table, so need to turn toward edge
		return (sharedColor.distHigh || sharedColor.distLow);		
	}	
    // test test
	@Override
	public void action() {
		turning = true;
		
		boolean temp = false;
		boolean isLeft = true;
		while (turning){
			//this could work for a forward moving turning mechanic
			if (!temp){
				sharedPilot.robot.stop();
				if (sharedColor.distHigh)
					sharedPilot.robot.steer(-correction);
				else if(sharedColor.distLow)
					sharedPilot.robot.steer(correction);
						
				temp = true;
			}
			
			if (sharedColor.distHigh && isLeft){
				temp = false;
				isLeft = false;
			}else if (sharedColor.distLow && !isLeft){
				temp = false;
				isLeft = true;
			}
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		turning = false;
		sharedPilot.robot.stop();
	}
}