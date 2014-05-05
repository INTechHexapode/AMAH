package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class SocketClient {

	Socket socket;
	PrintWriter out;
	InputStreamReader reader;
	BufferedReader input;
	public enum Request
	{
		CLOSE,
		LIN_ACC_X,
		LIN_ACC_Y,
		LIN_ACC_Z,
		GYRO_X,
		GYRO_Y,
		GYRO_Z,
		AZIMUTH,
		PITCH,
		ROLL
	};
	
	public SocketClient()
	{
		try {
			socket = new Socket("192.168.43.1",8080);
			out = new PrintWriter(socket.getOutputStream());			
			reader = new InputStreamReader(socket.getInputStream());
			input = new BufferedReader(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close()
	{
		sendMessage(Request.CLOSE);
		out.close();
		try {
			reader.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String sendMessage(Request request) {
		String s = null;
		out.println(request);
		out.flush();
		try {
			s = input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}