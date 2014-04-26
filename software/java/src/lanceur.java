import hexapode.Hexapode;

import java.util.Scanner;

import serial.Serial;
import serial.SerialManager;
import test.TestCoordinationPattesSimulation;
import test.TestEngine;
import util.Sleep;

public class lanceur {

	public static void main(String[] args) {
		SerialManager serialmanager;
		try {
		    /*serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");*/
			Hexapode hexa = new Hexapode(null);
			System.out.println("Attente");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			TestCoordinationPattesSimulation test = new TestCoordinationPattesSimulation(hexa, 1000, 50, 1, true, false);
			TestEngine testEngine = new TestEngine(test);
			testEngine.start();

			/*for(int i = -100; i < 100; i++)
			{
			    System.out.println(i);
    			hexa.pattes[0][0].goto_etat(new EtatPatte(i, 100., -100.));
                Sleep.sleep(100);
			}*/
			
			hexa.desasserv();
			
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
			//serie.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

}
