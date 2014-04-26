import hexapode.GoToException;
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
//		    serialmanager = new SerialManager();
//			Serial serie = serialmanager.getSerial("serieAsservissement");
			Hexapode hexa = new Hexapode(null);
 			System.out.println("Attente");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();

			hexa.va_au_point(0, 0);
			
			scanner.close();
//			serie.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	public static void cercle(Hexapode hexa) throws GoToException
	{
        Sleep.temps_defaut = 300;
        for(int i = 0; i < 18; i++)
        {
            hexa.goto_etat("101010");
            hexa.goto_etat("010101");
            if(i % 3 == 2)
                hexa.setDirectioneRelatif(1);
        }	    
	}
	
	public static void marche_scriptee(Hexapode hexa) throws GoToException
	{
        Sleep.temps_defaut = 1000;
        for(int i = 0; i < 30; i++)
        {
            hexa.goto_etat("101010");
            hexa.goto_etat("010101");
        }
        hexa.arret();
	}

	// TODO: tester
	   public static void autre_marche_scriptee(Hexapode hexa) throws GoToException
	    {
	        Sleep.temps_defaut = 1000;
	        for(int i = 0; i < 30; i++)
	        {
	            if(i % 2 == 0)
	                hexa.goto_etat("101010");
	            else
                    hexa.goto_etat("010101");
	            hexa.goto_etat("111111");
                hexa.goto_etat("000000");
	        }
	        hexa.arret();
	    }

	public static void acceleration(Hexapode hexa) throws GoToException
	{
        Sleep.temps_defaut = 500;
	    for(int i = 0; i < 10; i++)
        {
            hexa.goto_etat("101010");
            Sleep.temps_defaut -= 20;
            hexa.goto_etat("010101");
            Sleep.temps_defaut -= 20;
        }
        for(int i = 0; i < 5; i++)
        {
            hexa.goto_etat("101010");
            hexa.goto_etat("010101");
        }	    
	}

	public static void test_validation(Hexapode hexa)
	{
        TestCoordinationPattesSimulation test = new TestCoordinationPattesSimulation(hexa, 10000, 50, 1, true, true);
        TestEngine testEngine = new TestEngine(test);
        testEngine.start();
	}
	
}
