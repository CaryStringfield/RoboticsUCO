import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class BehaviorMain {
	static Arbitrator arby;
	
	public static void main (String[] args) {
		RegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.C);
		SharedIRSensor ir = new SharedIRSensor();
		
		Behavior b1 = new BehaviorForward(left, right);
		Behavior b2 = new BehaviorProximity(left, right, ir);
		Behavior[] behave = {b1, b2};
		arby = new Arbitrator(behave);
		arby.start();		
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
