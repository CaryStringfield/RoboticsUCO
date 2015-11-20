import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.hardware.sensor.HiTechnicCompass;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.hardware.sensor.NXTUltrasonicSensor;

// Main Class that starts up the sensors and contains the Arbitrator
public class BehaviorMain {
	// used for managing behaviors
	static Arbitrator arby;
	
	// main method called on start-up
	// defines behaviors and sensors
	public static void main (String[] args) {
		
		// used so motor control can be shared between behaviors
		SharedDifferentialPilot pilot = new SharedDifferentialPilot();
		// used so multiple behaviors can read from the IR and Color sensors
		SharedIRSensor ir = new SharedIRSensor();
        SharedUltraSonicSensor lus = new SharedUltraSonicSensor(SensorPort.S3);
        SharedUltraSonicSensor rus = new SharedUltraSonicSensor(SensorPort.S4);
		SharedColorSensor clr = new SharedColorSensor();
		
		// default behavior, robot simply drives forward
		Behavior bForward = new BehaviorForward(pilot);
		// for when an edge is detected in front of the robot
		Behavior bEdgeAvoid = new BehaviorAvoidEdge(pilot, lus,rus); 
		// steer left or right based on reading from the color sensor
		Behavior bSteer = new BehaviorSteer(pilot, lus,rus);
		// This behavior allow the robot to be shutdown on button press
		Behavior die = new BehaviorDie();
		
		// proximity stuff
		SharedGrabber grabber = new SharedGrabber();
		Behavior getObject = new BehaviorProximity(pilot, clr, grabber);
		
		// for find beacon
		Behavior bAim = new BehaviorAimAtBeacon(pilot, ir);
		Behavior bStop = new BehaviorStopAtBeacon(pilot,ir,grabber);
		Behavior bCorrect = new BehaviorSeekCorrect(pilot,ir);
		
		// the behavior priority list for the robot
		//Behavior[] behave = {bForward, bSteer, bEdgeAvoid, die};
		//Behavior[] behave = {bForward, bAim, bStop, die};
		Behavior[] behave = {bForward, bCorrect, bAim,  bStop, getObject, bSteer, bEdgeAvoid, die};
		
		arby = new Arbitrator(behave);
		arby.start();	
	}
}

class SharedColorSensor extends Thread {
	EV3ColorSensor clr = new EV3ColorSensor(SensorPort.S2);
	SampleProvider sp = clr.getRedMode();
	// This is the desired value to be read from the sensor. The values will
	// vary based upon the table color. This is where we calibrate the sensor.
	//public float normal = .1f;
	// this is the +-error from the normal that is allowed to be read
	//public float tolerance = .03f;
	// whether the read value is high or low
	//boolean distLow, distHigh;
	public float value = 0f;
	
	// the sensor is initialized to false and thread is started
	SharedColorSensor() {
		//this.distLow = false;
		//this.distHigh = false;
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while(true){		
			// retrieve sample from sensor
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			
			value = (float)sample[0];
			// if value is low (color sensor too far off the table)
			/*if(sample[0] < normal - tolerance){
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
			LCD.drawString("sample size: " + sample.length, 0, 5);*/
			Thread.yield();
		}
	}
}

class SharedIRSensor extends Thread {
	EV3IRSensor ir = new EV3IRSensor(SensorPort.S1);
	// sets the IR sensor to the distance mode which return the distance from an object
	SampleProvider sp = ir.getDistanceMode();
	public int distance = 255;
	public int distanceSeek = 255;
	public int bearing = 255;
	String mode; //holds mode state
	
	//thread is started
	SharedIRSensor() {
		this.setDistance(); //default to distance mode
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while (true) {
			// retrieve sample
			if (mode == "seek"){
				float[] sample = new float[8];
				sp.fetchSample(sample, 0);
				bearing = (int)sample[0];
				distanceSeek = (int)sample[1];
				LCD.drawString("distance: " + distanceSeek + "                             ", 0, 0);
            	LCD.drawString("bearing: " + bearing + "                             ", 0, 3);
			}else{
				float[] sample = new float[sp.sampleSize()];
				sp.fetchSample(sample, 0);
				// store sample for use by Behaviors
				distance = (int)sample[0];
				LCD.drawString("distance: " + distance + "                             ", 0, 0);
			}
			Thread.yield();
		}		
	}
	
	public void setSeek() {
		this.mode = "seek";
		sp = ir.getSeekMode();
	}
	
	public void setDistance() {
		this.mode = "distance";
		sp = ir.getDistanceMode();
	}
	
	public void tmpSeek(){
		if (mode == "seek"){
			float[] sample = new float[8];
			sp.fetchSample(sample, 0);
			bearing = (int)sample[0];
			distanceSeek = (int)sample[1];
		}else{
			sp = ir.getSeekMode();
			float[] sample = new float[8];
			sp.fetchSample(sample, 0);
			bearing = (int)sample[0];
			distanceSeek = (int)sample[1];
			sp = ir.getDistanceMode();
		}
	}
	public void tmpDist(){
		if (mode == "seek"){
			sp = ir.getDistanceMode();
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			distance = (int)sample[0];
			sp = ir.getSeekMode();
		}else{
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			distance = (int)sample[0];
		}
	}
}

class SharedUltraSonicSensor extends Thread {	
    NXTUltrasonicSensor us;
    // sets the IR sensor to the distance mode which return the distance from an object
    SampleProvider sp;
    public float distance = 255.0f;
    
    //thread is started
    SharedUltraSonicSensor(Port p) {
    	this.us = new NXTUltrasonicSensor(p);
    	this.sp = this.us.getDistanceMode();
        this.setDaemon(true);
        this.start();
    }
    
    public void run() {
        while (true) {
            // retrieve sample
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            // store sample for use by Behaviors
            distance = (float)sample[0];
            
            if (us.getPort() == SensorPort.S3)
            	LCD.drawString("Ldistance: " + distance + "                             ", 0, 1);
            else
    			LCD.drawString("Rdistance: " + distance + "                             ", 0, 2);
            
            Thread.yield();
        }
    }
}

class SharedCompass extends Thread {
	HiTechnicCompass compass = new HiTechnicCompass(SensorPort.S2);
	// sets the IR sensor to the distance mode which return the distance from an object
	SampleProvider sp = compass.getAngleMode();
	public int bearing = 255;
	
	//thread is started
	SharedCompass() {
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while (true) {
			// retrieve sample
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			// store sample for use by Behaviors
			bearing = (int)sample[0];
			Thread.yield();
		}
	}
}
class SharedDifferentialPilot extends Thread{
	// diameter of the wheels
	private double diam = DifferentialPilot.WHEEL_SIZE_EV3;
	// width of distance between wheels
	private double trackwidth = 14.5;
	// forward speed
	private float travelSpeed = 8;
	// turn speed
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

class SharedGrabber extends Thread{
	//public EV3MediumRegulatedMotor motor = new EV3MediumRegulatedMotor(MotorPort.D);
	public RegulatedMotor motor = Motor.D;
	public int angle = 2000;
	public String state = "open";
	public boolean alive = true;
	
	public SharedGrabber(){
		this.setDaemon(true);;
		this.start();
	}
	
	public void run(){
		while(alive){
			Thread.yield();
		}
		motor.close();
	}
	
	public void closeClaw(){
		if(state=="open"){
			state = "closing";
			motor.rotate(-angle);
			state = "closed";
		}
	}
	public void openClaw(){
		if(state=="closed"){
			state = "opening";
			motor.rotate(angle);
			state = "open";
		}
	}
}


