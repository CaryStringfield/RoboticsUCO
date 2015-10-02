import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class BehaviorMain {
	static Arbitrator arby;
	
	public static void main (String[] args) {
		SharedDifferentialPilot pilot = new SharedDifferentialPilot();
		//pilot.robot.rotate(90);
		//pilot.alive = false;
		
		//RegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.B);
		//RegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.C);
		SharedIRSensor ir = new SharedIRSensor();
		//SharedTouchSensor tch = new SharedTouchSensor();
		SharedColorSensor clr = new SharedColorSensor();
		
		Behavior bForward = new BehaviorForward(pilot);
		Behavior bEdgeAvoid = new BehaviorAvoidEdge(pilot, ir);
		//Behavior b2 = new BehaviorProximity(left, right, ir);
		//Behavior b3 = new BehaviorTouch(left, right, tch);
		Behavior bTurnLeft = new BehaviorTurnLeft(pilot, clr);
		Behavior bTurnRight = new BehaviorTurnRight(pilot, clr);
		Behavior bSteer = new BehaviorSteer(pilot, clr); 
		Behavior die = new BehaviorDie();
		
		Behavior[] behave = {bForward, bSteer, bEdgeAvoid, die};
		arby = new Arbitrator(behave);
		arby.start();	
	}
}

class SharedColorSensor extends Thread {
	EV3ColorSensor clr = new EV3ColorSensor(SensorPort.S3);
	SampleProvider sp = clr.getRedMode();
	public float normal = .15f;
	public float tolerance = .03f;
	boolean distLow, distHigh;
	
	SharedColorSensor() {
		this.distLow = false;
		this.distHigh = false; 
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while(true){
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			if(sample[0] < normal - tolerance){
				distLow = true;
				distHigh=false;
			}else if(sample[0] > normal + tolerance){
				distHigh = true;
				distLow = false;
			}else{
				distLow = false;
				distHigh= false;
			}
			LCD.drawString("sample: " + sample[0] + " ", 0, 0); //for debugging later
			LCD.drawString("distLow: " + distLow + " ", 0, 1); //for debugging later
			LCD.drawString("distHigh: " + distHigh + " ", 0, 2); //for debugging later
			LCD.drawString("normal: " + normal + " ", 0, 3); //for debugging later
			LCD.drawString("tolerance: +-" + tolerance + " ", 0, 4); //for debugging later
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
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			distance = (int)sample[0];
			//LCD.drawString("Distance: " + distance + " ", 0, 0);
			Thread.yield();
		}
	}
}

class SharedTouchSensor extends Thread {
	SensorModes sensor = new EV3TouchSensor(SensorPort.S2);
	SampleProvider touch = sensor.getMode("Touch");
	public boolean contact;
	
	SharedTouchSensor() {
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while (true) {
			float[] sample = new float[touch.sampleSize()];
			touch.fetchSample(sample, 0);
			if((int)sample[0] == 1)
				contact = true;
			else
				contact = false;
				
			Thread.yield();
		}
	}
}

class SharedDifferentialPilot extends Thread{
	private double diam = DifferentialPilot.WHEEL_SIZE_EV3;
	private double trackwidth = 14.5;
	private float travelSpeed =10;
	private float rotateSpeed = 50;
	
	// Motors on ports B and C
	public DifferentialPilot robot = new DifferentialPilot(diam, trackwidth, Motor.B, Motor.C);
	public boolean alive = true;
	
	public SharedDifferentialPilot(){
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

