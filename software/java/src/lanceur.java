import hexapode.Hexapode;

import java.util.Scanner;

import serial.Serial;
import serial.SerialManager;
import util.Sleep;

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

//			StandStillTest standstilltest = new StandStillTest(hexa, 100, 600., 0., true, true);
//			TestEngine testengine = new TestEngine(standstilltest);
//			testengine.start();
//			hexa.stand_up();

			
			for(int i = 0; i < 10; i++)
			{
    			hexa.goto_etat("000000");
                Sleep.sleep(1000);
                hexa.goto_etat("101101");
                Sleep.sleep(1000);
			}
			hexa.desasserv();

			scanner.close();
			serie.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

}
