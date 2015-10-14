import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

// Main Class that starts up the sensors and contains the Arbitrator
public class BehaviorMain {
	// used for managing behaviors
	static Arbitrator arby;
	
	// main method called on start-up
	public static void main (String[] args) {
		// used so motor control can be shared between behaviors
		SharedDifferentialPilot pilot = new SharedDifferentialPilot();
		// used so multiple behaviors can read from the IR and Color sensors
		SharedIRSensor ir = new SharedIRSensor();
		SharedColorSensor clr = new SharedColorSensor();
		
		// default behavior, robot simply drives forward
		Behavior bForward = new BehaviorForward(pilot);
		// for when an edge is detected in front of the robot
		Behavior bEdgeAvoid = new BehaviorAvoidEdge(pilot, ir); 
		// steer left or right based on reading from the color sensor
		Behavior bSteer = new BehaviorSteer(pilot, clr);
		// This behavior allow the robot to be shutdown on button press
		Behavior die = new BehaviorDie();
		
		// the behavior priority list for the robot
		Behavior[] behave = {bForward, bSteer, bEdgeAvoid, die};
		arby = new Arbitrator(behave);
		arby.start();	
	}
}

class SharedColorSensor extends Thread {
	EV3ColorSensor clr = new EV3ColorSensor(SensorPort.S3);
	SampleProvider sp = clr.getRedMode();
	// this is the desired value to be read from the sensor
	public float normal = .1f;
	// this is the +-error from the normal that is allowed to be read
	public float tolerance = .03f;
	// whether the read value is high or low
	boolean distLow, distHigh;
	
	SharedColorSensor() {
		this.distLow = false;
		this.distHigh = false;
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while(true){		
			// retrieve sample from sensor
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			
			// if value is low (color sensor too far off the table)
			if(sample[0] < normal - tolerance){
				distLow = true;
				distHigh = false;
			}
			// if value is high (color sensor too far on the table)
			else if(sample[0] > normal + tolerance){
				distHigh = true;
				distLow = false;
			}
			// color sensor is placed perfectly on the edge
			else{
				distLow = false;
				distHigh= false;
			}
			// debug output
			LCD.drawString("sample: " + sample[0] + " ", 0, 0);
			LCD.drawString("distLow: " + distLow + " ", 0, 1);
			LCD.drawString("distHigh: " + distHigh + " ", 0, 2);
			LCD.drawString("normal: " + normal + " ", 0, 3);
			LCD.drawString("tolerance: +-" + tolerance + " ", 0, 4);
			LCD.drawString("sample size: " + sample.length, 0, 5);
			Thread.yield();
		}
	}
}

class SharedIRSensor extends Thread {
	EV3IRSensor ir = new EV3IRSensor(SensorPort.S1);
	SampleProvider sp = ir.getDistanceMode();
	public int distance = 255;
	
	SharedIRSensor() {
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while (true) {
			// retrieve sample
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			// store sample for use by Behaviors
			distance = (int)sample[0];
			Thread.yield();
		}
	}
}

class SharedDifferentialPilot extends Thread{
	private double diam = DifferentialPilot.WHEEL_SIZE_EV3;
	private double trackwidth = 14.5;
	private float travelSpeed = 10;
	private float rotateSpeed = 50;
	
	// Motors on ports B and C (left and right respectively)
	public DifferentialPilot robot = new DifferentialPilot(diam, trackwidth, Motor.B, Motor.C);
	public boolean alive = true;
	
	public SharedDifferentialPilot(){
		// set the speed of the motors
		robot.setTravelSpeed(travelSpeed);
		robot.setRotateSpeed(rotateSpeed);
		
		this.setDaemon(true);
		this.start();
	}
	
	public void run(){
		while(alive){
			Thread.yield();
		}
	}
}

