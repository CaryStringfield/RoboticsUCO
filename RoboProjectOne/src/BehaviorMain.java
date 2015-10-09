import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class BehaviorMain {
	static Arbitrator arby;
	
	public static void main (String[] args) {
		SharedDifferentialPilot pilot = new SharedDifferentialPilot();
		SharedIRSensor ir = new SharedIRSensor();
		SharedColorSensor clr = new SharedColorSensor();
		
		Behavior bForward = new BehaviorForward(pilot);
		Behavior bEdgeAvoid = new BehaviorAvoidEdge(pilot, ir); 
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
	public float normal = .1f;
	public float tolerance = .03f;
	public float angleTolerance = .01f;
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
				distHigh = false;
			}
			else if(sample[0] > normal + tolerance){
				distHigh = true;
				distLow = false;
			}else{
				distLow = false;
				distHigh= false;
			}
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
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			distance = (int)sample[0];
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

