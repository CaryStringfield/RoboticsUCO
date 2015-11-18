import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.*;

//stop if the robot is within 40 cm of an object and move about
	public class BehaviorProximity implements Behavior {
		// used to find objects
		private SharedIRSensor ir;
		// used for stopping and rotating the robot
		private SharedDifferentialPilot sharedPilot;
		//grabber
		private SharedGrabber grabber;		
		//detect if the object is small enough to move
		private boolean movable;
		
		public BehaviorProximity(SharedDifferentialPilot sharedPilot, SharedIRSensor ir, SharedGrabber grabber){
			this.ir = ir;
			ir.setDistance();
			this.sharedPilot = sharedPilot;
			this.movable = true;
			this.grabber = grabber;
		}
		
		//if the distance is less within 20 cm then the thread takes control
		@Override
		public boolean takeControl() {
			ir.tmpDist();
			return (ir.distance < 20 && ir.distance > 1);
		}
		
		// check to see if the object can be moved and grab it
		@Override
		public void action() {
			ir.setDistance();
			//look to the left
			//sharedPilot.robot.rotate(30);
			//if (ir.distance < 25) {
				//look to the right
				//sharedPilot.robot.rotate(-60);
				//if (ir.distance < 25) {
					//align again
					//sharedPilot.robot.rotate(30);
					
					//go forward and grab the object
					//while (ir.distance != 0) {
					//	sharedPilot.robot.forward();					
					//}
					sharedPilot.robot.stop();
					
					//close claw and celebrate
					grabber.closeClaw();
					while(grabber.state=="closing");
					sharedPilot.robot.rotate(360);
					grabber.openClaw();
				//}
				//else {
				//	sharedPilot.robot.rotate(-15);
				//	this.movable = false;
				//}
			//}
			//else {
			//	sharedPilot.robot.rotate(15);
			//	this.movable = false;		
			//}							
		}
		
		@Override
		public void suppress() {
			}
		}
	
	