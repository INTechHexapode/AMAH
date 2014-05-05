package com.example.socketandroid.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.socketandroid.MainActivity;
import com.example.socketandroid.sensors.SensorsPreviewActivity;

public class SocketServer extends AsyncTask<Void, Integer, Void> {

	private final static String TAG = "INTech-Socket";
	private int port = 8080;
	private PrintWriter out;
	private static Handler messageHandler;
	private static ServerSocket server;
	private static Socket socket;
	private static boolean isServerEnabled = true;
	
	public static void stop() {
		Log.d(TAG, "Fermeture de la socket");
		isServerEnabled = false;
		try {
			server.close();

			// Affichage à l'écran des infos
			Message infoMessage = messageHandler.obtainMessage(
					MainActivity.MESSAGE_UPDATE_SERVER_STATUS, "Déconnecté");
			messageHandler.sendMessage(infoMessage);
		} catch (IOException e) {
			Log.w(TAG, "Erreur socket: " + e.getMessage());
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.i(TAG, "Création du thread d'écoute");
		try {
			// Création du serveur
			server = new ServerSocket(port);

			socket = server.accept();

			Log.i(TAG, "Client connecté");

			out = new PrintWriter(socket.getOutputStream());
			InputStreamReader reader = new InputStreamReader(socket.getInputStream());
			BufferedReader input = new BufferedReader(reader);
			while(isServerEnabled)
			{
				String request = input.readLine();
				switch(String.valueOf(request))
				{
				case "LIN_ACC_X":
					out.println(SensorsPreviewActivity.getxLinearAccelerometer());
					out.flush();
					break;
				case "LIN_ACC_Y":
					out.println(SensorsPreviewActivity.getyLinearAccelerometer());
					out.flush();
					break;
				case "LIN_ACC_Z":
					out.println(SensorsPreviewActivity.getzLinearAccelerometer());
					out.flush();
					break;
				case "GYRO_X":
					out.println(SensorsPreviewActivity.getxGyroscope());
					out.flush();
					break;
				case "GYRO_Y":
					out.println(SensorsPreviewActivity.getyGyroscope());
					out.flush();
					break;
				case "GYRO_Z":
					out.println(SensorsPreviewActivity.getzGyroscope());
					out.flush();
					break;
				case "AZIMUTH":
					out.println(SensorsPreviewActivity.getAzimuth());
					out.flush();
					break;
				case "PITCH":
					out.println(SensorsPreviewActivity.getPitch());
					out.flush();
					break;
				case "ROLL":
					out.println(SensorsPreviewActivity.getRoll());
					out.flush();
					break;
				case "CLOSE":
					Log.d(TAG,"Stop le ");
					isServerEnabled = false;
					break;
				default:
					continue;
				}
			}
			input.close();
			reader.close();
			stop();
		} catch (UnknownHostException e) {
			Log.w(TAG, "Erreur socket: " + e.getMessage());
		} catch (IOException e) {
			Log.w(TAG, "Erreur socket: " + e.getMessage());
		}

		return null;
	}
	
}
