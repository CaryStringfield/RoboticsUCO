import lejos.hardware.*;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.*;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.hardware.port.*;
import lejos.utility.Delay;


public class RobotTests {

	public static void main(String[] args) {
		Button.waitForAnyPress();
		
		// Test IR sensor on robot port #4,
		//   query the sensor 10 times,
		//   wait 1.5 seconds between queries
		testIRSensor(SensorPort.S4, 10, 1500);
		
		// Test Color sensor on robot port #3,
		//   query the sensor 10 times,
		//   wait 1.5 seconds between queries
		testColorSensor(SensorPort.S3, 10, 1500);
		testIDColorSensor(SensorPort.S3, 10, 1500);
		
		// Test for the touch sensor, port 2
		// query sensor 10 times,
		// wait 1.5 seconds between queries
		testTouchSensor(SensorPort.S2, 10, 1500);
		
		
		// Motor Code
		UnregulatedMotor b = new UnregulatedMotor(MotorPort.B);
		UnregulatedMotor c = new UnregulatedMotor(MotorPort.C);
		b.setPower(10);
		c.setPower(10);
		
		b.forward();
		c.forward();
		// go forward for 1.5 seconds
		Delay.msDelay(1500);
		
		b.flt();
		c.flt();
		b.close();
		c.close();
	}

	// Port: port that sensor is connected to
	// iterations: number of times to query the sensor
	// delay: number of milliseconds between iterations	
	private static void testIDColorSensor(Port port, int iterations, long delay){
		// get the IR sensor on the specific robot port
		EV3ColorSensor sensor = new EV3ColorSensor(port);
		
		// IR Sensor
		for (int i=0; i < iterations; i++){
			SampleProvider color = sensor.getColorIDMode();
			
			int colorID = sensor.getColorID();
			
			// Print newest sample value
			System.out.println(""+colorID);
			
			// delay before next query to sensor
			Delay.msDelay(delay);
		}
		
		// close the sensor
		((Device) sensor).close();
	}

	
	// Port: port that sensor is connected to
	// iterations: number of times to query the sensor
	// delay: number of milliseconds between iterations	
	private static void testColorSensor(Port port, int iterations, long delay){
		// get the IR sensor on the specific robot port
		EV3ColorSensor sensor = new EV3ColorSensor(port);
		
		// IR Sensor
		for (int i=0; i < iterations; i++){
			SampleProvider color = sensor.getRGBMode();

			// stack a filter on the sensor that gives the running average of the last 5 samples
			SampleProvider average = new MeanFilter(color, 5);

			// initialize an array of floats for fetching samples
			float[] sample = new float[average.sampleSize()];
			
			// fetch a sample
			average.fetchSample(sample, 0);
			
			// Print newest sample value
			System.out.println("("+sample[0]+","+sample[1]+","+sample[2]+")");
			
			// delay before next query to sensor
			Delay.msDelay(delay);
		}
		
		// close the sensor
		((Device) sensor).close();
	}
	
	// Port: port that sensor is connected to
	// iterations: number of times to query the sensor
	// delay: number of milliseconds between iterations
	private static void testIRSensor(Port port, int iterations, long delay){
		// get the IR sensor on the specific robot port
		SensorModes sensor = new EV3IRSensor(port);
		
		// IR Sensor
		for (int i=0; i < iterations; i++){
			SampleProvider distance= sensor.getMode("Distance");

			// stack a filter on the sensor that gives the running average of the last 5 samples
			SampleProvider average = new MeanFilter(distance, 5);

			// initialize an array of floats for fetching samples
			float[] sample = new float[average.sampleSize()];
			
			// fetch a sample
			average.fetchSample(sample, 0);
			
			// Print newest sample value
			System.out.println(sample[0]);
			
			// delay before next query to sensor
			Delay.msDelay(delay);
		}
		
		// close the sensor
		((Device) sensor).close();
	}

	// Port: port that sensor is connected to
	// iterations: number of times to query the sensor
	// delay: number of milliseconds between iterations
	private static void testTouchSensor(Port port, int iterations, long delay){
		//Port port = LocalEV3.get().getPort("S2");
		
		// get the Touch sensor on the specific robot port
		SensorModes sensor = new EV3TouchSensor(port);
		
		// Touch Sensor
		for (int i=0; i < iterations; i++){
			SampleProvider touch = sensor.getMode("Touch");

			// stack a filter on the sensor that gives the running average of the last 5 samples
			SampleProvider average = new MeanFilter(touch, 5);

			// initialize an array of floats for fetching samples
			float[] sample = new float[average.sampleSize()];
			
			// fetch a sample
			average.fetchSample(sample, 0);
			
			// Print newest sample value
			System.out.println(sample[0]);
			
			// delay before next query to sensor
			Delay.msDelay(delay);
		}
		
		// close the sensor
		((Device) sensor).close();
	}
	
	// code for ULTRA-Sonic found at http://sourceforge.net/p/lejos/wiki/Sensor%20Framework/

}