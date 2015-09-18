
/* 
 * Whith IR facing down should move around the table
 * 
 */

import lejos.hardware.*;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.navigation.*;
import lejos.utility.*;


public class RoundTheTable {
	
	public static void main(String[] args) {
		double diam = DifferentialPilot.WHEEL_SIZE_EV3;
		double trackwidth = 15.2;
		boolean homeFound = false;
		
		// Motors on ports B and C
		DifferentialPilot rov3r = new DifferentialPilot(diam, trackwidth, Motor.B, Motor.C);
		
		// put IR sensor in port 1
		EV3IRSensor ir = new EV3IRSensor(SensorPort.S1);
		SensorMode distMode = ir.getMode("Distance");
		int distance = 10;
		
		while (!homeFound)
		{
		rov3r.forward();
		int turns = 0;
		while (!Button.ESCAPE.isDown()) {
			
			float[] sample = new float[distMode.sampleSize()];
			distMode.fetchSample(sample, 0);
				distance = (int)sample[0];
				LCD.drawString("DiST: ", 0, 2);
				LCD.drawInt(distance, 3,  5, 2);
				
				if (distance > 10 ) {
					rov3r.travel(-20);
					rov3r.rotate(90);
					rov3r.forward();
					turns ++;
				}
				Delay.msDelay(100);				
		}
			if(turns >= 4)
				homeFound = true;
		}
		
		ir.close();
	}

}

