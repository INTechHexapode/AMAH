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
	private Sensor magnetic;

	private static float xGyroscope;
	private static float yGyroscope;
	private static float zGyroscope;
	private static float xAccelerometer;
	private static float yAccelerometer;
	private static float zAccelerometer;
	private static float xLinearAccelerometer;
	private static float yLinearAccelerometer;
	private static float zLinearAccelerometer;
	private static float xMagnetic;
	private static float yMagnetic;
	private static float zMagnetic;
	
	private float[] resultMatrix = new float[9];
	float[] values = new float[3];
	float[] acceleromterVector = new float[3];
	float[] magneticVector = new float[3];

	private static float azimuth;

	private static float pitch;

	private static float roll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sensors_preview);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_UI);

	}

	@Override
	protected void onResume() {
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_UI);
		super.onResume();
	}

	@Override
	public void onPause() {
		sensorManager.unregisterListener(this, accelerometer);
		sensorManager.unregisterListener(this, linearAccelerometer);
		sensorManager.unregisterListener(this, gyroscope);
		sensorManager.unregisterListener(this, magnetic);
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
			acceleromterVector = event.values;
		}
		else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
		{
			xLinearAccelerometer = event.values[0];
			yLinearAccelerometer = event.values[1];
			zLinearAccelerometer = event.values[2];
		}
		else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
	        // Valeur du vecteur du champ magnétique (x,y,z)
	        xMagnetic = event.values[0];
	        yMagnetic = event.values[1];
	        zMagnetic = event.values[2];
	        magneticVector = event.values;
		}
		
		// Demander au sensorManager la matric de Rotation (resultMatric)
		SensorManager.getRotationMatrix(resultMatrix, null, acceleromterVector, magneticVector);
		// Demander au SensorManager le vecteur d'orientation associé (values)
		SensorManager.getOrientation(resultMatrix, values);
		// l'azimuth
		azimuth =(float) Math.toDegrees(values[0]);
		// le pitch
		pitch = (float) Math.toDegrees(values[1]);
		// le roll
		roll = (float) Math.toDegrees(values[2]);

	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public static float getxGyroscope() {
		return xGyroscope;
	}

	public static float getyGyroscope() {
		return yGyroscope;
	}

	public static float getzGyroscope() {
		return zGyroscope;
	}

	public static float getxAccelerometer() {
		return xAccelerometer;
	}

	public float getyAccelerometer() {
		return yAccelerometer;
	}

	public float getzAccelerometer() {
		return zAccelerometer;
	}

	public static float getxLinearAccelerometer() {
		return xLinearAccelerometer;
	}

	public static float getyLinearAccelerometer() {
		return yLinearAccelerometer;
	}

	public static float getzLinearAccelerometer() {
		return zLinearAccelerometer;
	}

	public static float getxMagnetic() {
		return xMagnetic;
	}

	public static float getyMagnetic() {
		return yMagnetic;
	}

	public static float getzMagnetic() {
		return zMagnetic;
	}
	
	public static float getAzimuth() {
		return azimuth;
	}

	public static float getPitch() {
		return pitch;
	}

	public static float getRoll() {
		return roll;
	}
}
