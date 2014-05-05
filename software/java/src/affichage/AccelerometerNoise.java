package affichage;

import java.util.ArrayList;

import socket.SocketClient;
import socket.SocketClient.Request;

public class AccelerometerNoise {

	static ArrayList<Integer> noise = new ArrayList<Integer>();
	static int t[];
	public static void main(String[] args) throws InterruptedException {
		SocketClient s = new SocketClient();
		t = new int[500];
		for (int i = 0; i <= 10000; i++) {
			String a = s.sendMessage(Request.LIN_ACC_X);
			float z = Float.parseFloat(a);
			if(z < 0)
				z = -z;
			z *= 1000;
			t[(int) z]++;
			System.out.println(i/100 + "%");
		}
		for(int i : t) {
				System.out.println(i);
		}
	}

}
