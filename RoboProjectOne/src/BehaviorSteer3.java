import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

public class BehaviorSteer3 implements Behavior {
	
	private SharedDifferentialPilot sharedPilot;
	private SharedColorSensor sharedColor;
	private boolean turning = false ;
	private int correction = 20;

	public  BehaviorSteer3(SharedDifferentialPilot pilot, SharedColorSensor clrs) {
		this.sharedPilot = pilot;
		this.sharedColor = clrs;
	}

	@Override
	public boolean takeControl() {
		// distHigh means sensor is more on the table, so need to turn toward edge
		return false;//(sharedColor.distHigh || sharedColor.distLow);		
	}	
	
	@Override
	public void action() {
		turning = true;
		
		boolean temp = false;
		boolean isLeft = true;
		while (turning){
			
			if (!temp){
				sharedPilot.robot.stop();
				//if (sharedColor.distHigh)
					sharedPilot.robot.steer(-correction);
				//-----------------------------------------------
				//take the third boolean variable into account
				//-----------------------------------------------
				//else if(sharedColor.distLow) {
						sharedPilot.robot.stop();
						sharedPilot.robot.steer(correction);
				//}					
				//-----------------------------------------------
				/*else if(sharedColor.distLow)
					sharedPilot.robot.steer(correction);
				 */	
				temp = true;
			}
			
			//if (sharedColor.distHigh && isLeft){
				temp = false;
				isLeft = false;
			//}else if (sharedColor.distLow && !isLeft){
				temp = false;
				isLeft = true;
			//}
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		turning = false;
		sharedPilot.robot.stop();
	}
}
