package com.example.socketandroid.socket;

import android.util.Log;

public final class SocketServerManager {
	
	private final String TAG = "INTech-SocketManager";
	private static volatile SocketServerManager instance = null;
	private SocketServer server;
	
	public synchronized static SocketServerManager getInstance() {
		if (instance == null) {
			instance = new SocketServerManager();
		}
		return instance;
	}
	
	public void startListeningSocket() {
		server = new SocketServer();
		server.execute();
	}
	
	public void stopListeningSocket() {
		if (server != null) {
			SocketServer.stop();
			Log.d(TAG, "fermeture de la socket");
		} else {
			Log.d(TAG, "aucune socket à fermer");
		}
	}

}
