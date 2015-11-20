import lejos.robotics.subsumption.Behavior;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.*;
import lejos.utility.Delay;

public class BehaviorSeekCorrect implements Behavior {
	// used for detecting the edge
	private SharedIRSensor ir;
	// used for stopping and rotating the robot
	private SharedDifferentialPilot sharedPilot;
	public boolean looking = true;//looking for or traveling to the beacon

	
	public BehaviorSeekCorrect(SharedDifferentialPilot sharedPilot, SharedIRSensor ir){
		this.ir = ir;
		this.sharedPilot = sharedPilot;
//		this.seek = ir.getMode("Seek");
		
	}
	
	
	@Override
	public boolean takeControl() {
		ir.tmpSeek();
		return (StateManager.getInstance().getState() == 3);
	}

	@Override
	public void action() {
		ir.setSeek();
		while(ir.bearing == 0){//turn
			sharedPilot.robot.rotate(-2);
		}//end while looking
		StateManager.getInstance().setState(0);
	}

	@Override	
	public void suppress() {}
}