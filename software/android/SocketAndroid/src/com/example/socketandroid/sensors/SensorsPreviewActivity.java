package com.example.socketandroid.sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.socketandroid.R;

public class SensorsPreviewActivity extends Activity implements SensorEventListener {

	@SuppressWarnings("unused")
	private final static String TAG = "INTech-SensorsPreview";

	private SensorManager sensorManager;
	private Sensor accelerometer;
	private Sensor linearAccelerometer;
	private Sensor gyroscope;

	private float xGyroscope;
	private float yGyroscope;
	private float zGyroscope;
	private float xAccelerometer;
	private float yAccelerometer;
	private float zAccelerometer;
	private static float xLinearAccelerometer;
	private static float yLinearAccelerometer;
	private static float zLinearAccelerometer;

	private static float xdistance = 0;
	private static float ydistance = 0;
	private static float zdistance = 0;
	
	private static final float ALPHA = 0.8f;

	private long oldTimeLinearAcceleration = 0;

	private static float xspeed = 0;

	private static float yspeed = 0;

	private static float zspeed = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sensors_preview);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

	}

	@Override
	protected void onResume() {
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
		super.onResume();
	}

	@Override
	public void onPause() {
		sensorManager.unregisterListener(this, accelerometer);
		sensorManager.unregisterListener(this, linearAccelerometer);
		sensorManager.unregisterListener(this, gyroscope);
		super.onPause();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// Écouter le changement du gyroscope:
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			// La vitesse angulaire autour de chaque axe
			xGyroscope = event.values[0];
			yGyroscope = event.values[1];
			zGyroscope = event.values[2];
		}
		else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			xAccelerometer = event.values[0];
			yAccelerometer = event.values[1];
			zAccelerometer = event.values[2];
		}
		else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
		{
			//TODO
	        float[] gravSensorVals = {0,0,0};
	        
	        gravSensorVals = lowPass(event.values.clone(), gravSensorVals);
	        
			long time = (System.currentTimeMillis() - oldTimeLinearAcceleration);

			if(Math.abs(xLinearAccelerometer) > 0.1 )
				xspeed += xLinearAccelerometer*time;
			if(Math.abs(yLinearAccelerometer) > 0.1 )
				yspeed += yLinearAccelerometer*time;
			if(Math.abs(zLinearAccelerometer) > 0.1 )
				zspeed+= zLinearAccelerometer*time;

			if(Math.abs(xspeed) > 0.5)
			xdistance += xspeed*time;
			if(Math.abs(xspeed) > 0.5)
				ydistance += yspeed*time;
			if(Math.abs(xspeed) > 0.5)
				zdistance += zspeed*time;

			xLinearAccelerometer = gravSensorVals[0];
			yLinearAccelerometer = gravSensorVals[1];
			zLinearAccelerometer = gravSensorVals[2];

			oldTimeLinearAcceleration = System.currentTimeMillis();
		}

	}
	
	protected float[] lowPass( float[] input, float[] output ) {
	    if ( output == null ) return input;     
	    for ( int i=0; i<input.length; i++ ) {
	        output[i] = output[i] + ALPHA * (input[i] - output[i]);
	    }
	    return output;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public float getxGyroscope() {
		return xGyroscope;
	}

	public float getyGyroscope() {
		return yGyroscope;
	}

	public float getzGyroscope() {
		return zGyroscope;
	}

	public float getxAccelerometer() {
		return xAccelerometer;
	}

	public float getyAccelerometer() {
		return yAccelerometer;
	}

	public float getzAccelerometer() {
		return zAccelerometer;
	}

	public float getxLinearAccelerometer() {
		return xLinearAccelerometer;
	}

	public float getyLinearAccelerometer() {
		return yLinearAccelerometer;
	}

	public float getzLinearAccelerometer() {
		return zLinearAccelerometer;
	}

	public static float getxdistance() {
		float x = xdistance;
		xdistance = 0;
		xspeed = 0;
		xLinearAccelerometer = 0;
		return x;
	}

	public static float getydistance() {
		float y = ydistance;
		ydistance = 0;
		yspeed = 0;
		yLinearAccelerometer = 0;
		return y;
	}

	public static float getzdistance() {
		float z = zdistance;
		zdistance = 0;
		zspeed = 0;
		zLinearAccelerometer = 0;
		return z;
	}

}
