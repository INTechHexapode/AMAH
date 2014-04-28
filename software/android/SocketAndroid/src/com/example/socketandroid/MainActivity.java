package com.example.socketandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ToggleButton;

import com.example.socketandroid.sensors.SensorsPreviewActivity;
import com.example.socketandroid.socket.SocketServerManager;
import com.example.socketandroid.wifi.WifiChangeTask;

public class MainActivity extends Activity {

	public static int MESSAGE_UPDATE_SERVER_STATUS = 1;

	public static int MESSAGE_DISPLAY_RESULT = 3;
	private final String TAG = "INTech";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Affichage de l'écran principal
		setContentView(R.layout.activity_main);

		// Empecher le verrouillage de l'écran
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		SocketServerManager.getInstance().stopListeningSocket();
	}	

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void openSocket(View v) {
		Log.d(TAG, "Activation de la socket");
		SocketServerManager.getInstance().stopListeningSocket();
		SocketServerManager.getInstance().startListeningSocket();
		startSensors(true);
	}

	public void toggleWifi(View v) {
		// Récupération de l'état du wifi
		ToggleButton button = (ToggleButton) findViewById(R.id.toggleWifiButton);
		boolean wifiStatus = button.isChecked();

		// Change l'état du wifi
		WifiChangeTask task = new WifiChangeTask(wifiStatus,
				(WifiManager) getSystemService(Context.WIFI_SERVICE),
				new ProgressDialog(MainActivity.this));
		task.execute();
	}

	public void startSensors(boolean openSocket) {
		Intent intent = new Intent(this, SensorsPreviewActivity.class);
		intent.putExtra("open_socket", openSocket);
		startActivity(intent);
	}
}