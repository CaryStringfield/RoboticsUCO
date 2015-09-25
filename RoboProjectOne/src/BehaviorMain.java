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
		//SharedColorSensor clr = new SharedColorSensor();
		
		Behavior bForward = new BehaviorForward(pilot);
		Behavior bEdgeAvoid = new BehaviorAvoidEdge(pilot, ir);
		//Behavior b2 = new BehaviorProximity(left, right, ir);
		//Behavior b3 = new BehaviorTouch(left, right, tch);
		//Behavior b4 = new BehaviorSenseEdge(left, right, clr);
		Behavior die = new BehaviorDie();
		
		Behavior[] behave = {bForward, bEdgeAvoid, die};
		arby = new Arbitrator(behave);
		arby.start();		
	}
}

class SharedColorSensor extends Thread {
	EV3ColorSensor clr = new EV3ColorSensor(SensorPort.S3);
	SampleProvider sp = clr.getRedMode();
	public float normal = .4F;
	boolean edge;
	
	SharedColorSensor() {
		this.edge = false;
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		float[] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
		if(sample[0] < normal)
			edge = true;
		else
			edge = false;
		LCD.drawString("on edge: " + edge + " ", 0, 2); //for debugging later
		Thread.yield();
	}
}

class SharedIRSensor extends Thread {
	EV3IRSensor ir = new EV3IRSensor(SensorPort.S1);
	SampleProvider sp = ir.getDistanceMode();
	public int control = 0;
	public int distance = 255;
	
	SharedIRSensor() {
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while (true) {
			float[] sample = new float[sp.sampleSize()];
			control = ir.getRemoteCommand(0);
			sp.fetchSample(sample, 0);
			if((int)sample[0] == 0)
				distance = 255;
			else
				distance = (int)sample[0];
			LCD.drawString("Control: " + control, 0, 0);
			LCD.drawString("Distance: " + distance + " ", 0, 1);
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
	private float travelSpeed = 10;
	private float rotateSpeed = 20;
	
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

