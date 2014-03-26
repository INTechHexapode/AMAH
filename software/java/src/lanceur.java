import hexapode.Hexapode;

import java.util.Scanner;

import serial.Serial;
import serial.SerialManager;
import serial.SerialManagerException;
import test.StandStillTest;
import test.TestEngine;

public class lanceur {

	public static void main(String[] args) {
		SerialManager serialmanager;
		try {
			serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");
			Hexapode hexa = new Hexapode(serie);
			System.out.println("Attente");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			StandStillTest standstilltest = new StandStillTest(hexa, 100, 600., 0., true, true);
			TestEngine testengine = new TestEngine(standstilltest);
			testengine.start();
			scanner.close();
			serie.close();
		} catch (SerialManagerException e) {
			e.printStackTrace();
		}
				
	}

}
