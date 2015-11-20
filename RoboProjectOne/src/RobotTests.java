import lejos.hardware.*;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.*;
import lejos.robotics.localization.*;
import lejos.robotics.navigation.*;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.hardware.port.*;
import lejos.utility.Delay;

/*
 * This was created to test the different kind of sensors to see how to use them
 * as well as to see what kind of data they would return.
 */
public class RobotTests {

	public static void main(String[] args) {
		// runTests();
		
		// Test Edge Seek
		//System.out.println("Edge Seek Test");
		//Button.waitForAnyPress();
		
		// run edge test
		//   left motor on B
		//   right motor on A
		//   IRSensor on S1
		//   min distance on IR to keep moving is 3.0
		//stopAtEdgeTest(MotorPort.B, MotorPort.C, SensorPort.S1, 3.0);

		// Test Odometry
		//System.out.println("Calibrate Test");
		//Button.waitForAnyPress();
		
		//EV3MediumRegulatedMotor motor = new EV3MediumRegulatedMotor(MotorPort.D);
		
		// close the claw
		//motor.rotate(-2000);
		// open the claw
		//motor.rotate(2000);

		
		//motor.stop();
		//motor.close();
		
		// wheel diameter
		//double wheelDiameter = DifferentialPilot.WHEEL_SIZE_EV3;
		// width between sides, I think?
		//double width = 14.5;
		//SharedDifferentialPilot pilot = new SharedDifferentialPilot();
		SharedGrabber grabber = new SharedGrabber();
		//pilot.robot.setRotateSpeed(10);

		// if crashed and claw is closed
		grabber.state = "closed";
		grabber.openClaw();
		while(grabber.state == "opening")
			
		//HiTechnicCompass compass = new HiTechnicCompass(SensorPort.S2);
		//compass.startCalibration();
		

		
		//grabber.closeClaw();
		//while(grabber.state == "closing");
		
		//pilot.robot.travel(15);
		//pilot.robot.stop();
		
		//grabber.openClaw();
		//while(grabber.state == "opening");
		
		//pilot.robot.travel(-15);
		//pilot.robot.stop();
		
		grabber.alive = false;
		
		//compass.stopCalibration();
		//compass.close();
		// used for making 'precise' movements with robot
		//DifferentialPilot robot = new DifferentialPilot(wheelDiameter,width,Motor.C,Motor.B);
		//OdometryPoseProvider pp = new OdometryPoseProvider(robot);

		// start position
		//System.out.println("Start: " + pp.getPose());
		//Button.waitForAnyPress();
		
		// performs various super cool tricks
		//robot.rotate(90);
		//robot.travel(20);
		//robot.travel(-20);
		//robot.rotate(-90);
		
		// end position

		System.out.println("End...");// + pp.getPose());
		//Button.waitForAnyPress();
		
	}
	
	// Test For Simple Edge Detect
	// 	robot moves forward until the
	//  IR sensor reads a large distance
	private static void stopAtEdgeTest(Port left, Port right, Port sp, double minDist){
		// Motor Code
		UnregulatedMotor b = new UnregulatedMotor(left);
		UnregulatedMotor c = new UnregulatedMotor(right);
		// get the IR sensor on the specific robot port
		SensorModes sensor = new EV3IRSensor(sp);

		// distance provider
		SampleProvider distance= sensor.getMode("Distance");
		// stack a filter on the sensor that gives the running average of the last 5 samples
		SampleProvider average = new MeanFilter(distance, 5);
		// initialize an array of floats for fetching samples
		float[] sample = new float[average.sampleSize()];
			
		// Full power
		b.setPower(100);
		c.setPower(100);
				
		// begin forward movement
		b.forward();
		c.forward();
		
		// check distance, if to far then stop
		do{
			// fetch a sample
			average.fetchSample(sample, 0);
		}while(sample[0] <= minDist);
				
		// 'softly' kill the motors
		b.flt();
		c.flt();
		
		// free the motors
		b.close();
		c.close();		
		// close the sensor
		((Device) sensor).close();
	}
	
	// ############################################
	// # Various motor and sensory test functions #
	// ############################################
	private void runTests(){
		System.out.println("Press button!");
		Button.waitForAnyPress();
		
		System.out.println("### IR Sensor ###");
		// Test IR sensor on robot port #4,
		//   query the sensor 10 times,
		//   wait 1.5 seconds between queries
		testIRSensor(SensorPort.S4, 10, 1500);
		Delay.msDelay(2000);

		System.out.println("### Color Sensor ###");
		// Test Color sensor on robot port #3,
		//   query the sensor 10 times,
		//   wait 1.5 seconds between queries
		testColorSensor(SensorPort.S3, 10, 1500);
		Delay.msDelay(2000);

		System.out.println("### Touch Sensor ###");
		// Test for the touch sensor, port 2
		// query sensor 10 times,
		// wait 1.5 seconds between queries
		testTouchSensor(SensorPort.S2, 20, 1500);
		Delay.msDelay(2000);

		//System.out.println("### Ultrasonic Sensor ###");
		// Test for the ultrasonic sensor, port 2
		// query sensor 10 times,
		// wait 1.5 seconds between queries
		//testUltrasonicSensor(SensorPort.S1, 10 ,1500);
		//Delay.msDelay(2000);		
		
		System.out.println("### Motors ###");
		// Test motors on ports B and C
		motorTest();
		
		System.out.println("Press button!");
		Button.waitForAnyPress();
	}
	
	private void motorTest(){
		// Motor Code
		UnregulatedMotor b = new UnregulatedMotor(MotorPort.B);
		UnregulatedMotor c = new UnregulatedMotor(MotorPort.C);
		
		// Full power
		b.setPower(100);
		c.setPower(100);
		
		// motor b goes backward
		//   motor c goes forward
		//   rotation ensues
		b.backward();
		c.forward();
		
		// go forward for 1.5 seconds
		Delay.msDelay(1500);
		
		// 'softly' kill the motors
		b.flt();
		c.flt();
		
		// free the motors
		b.close();
		c.close();
	}

	// Port: port that sensor is connected to
	// iterations: number of times to query the sensor
	// delay: number of milliseconds between iterations	
	private static void testColorSensor(Port port, int iterations, long delay){
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
	// Port: port that sensor is connected to
	// iterations: number of times to query the sensor
	// delay: number of milliseconds between iterations
	private static void testUltrasonicSensor(Port port, int iterations, long delay){
		// get the IR sensor on the specific robot port
		SensorModes sensor = new EV3UltrasonicSensor(port);
		
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

}