import hexapode.Hexapode;
import hexapode.markov.EtatPatte;
import hexapode.markov.EtatPatteTest2;

import java.util.Scanner;

import serial.Serial;
import serial.SerialManager;
import serial.SerialManagerException;
import test.StandStillTest;
import test.TestEngine;
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

			for(int i = -100; i < 100; i++)
			{
			    System.out.println(i);
    			hexa.pattes[0][0].goto_etat(new EtatPatte(i, 100., -100.));
                Sleep.sleep(100);
			}
			
			hexa.desasserv();
			
//			StandStillTest standstilltest = new StandStillTest(hexa, 100, 600., 0., true, true);
//			TestEngine testengine = new TestEngine(standstilltest);
//			testengine.start();
//			hexa.stand_up();

/*			
			System.out.println(hexa);
			for(int i = 0; i < 2; i++)
			{
    			hexa.goto_etat("111110");
                System.out.println(hexa);
                Sleep.sleep(1000);
                hexa.goto_etat("111111");
                System.out.println(hexa);
                Sleep.sleep(1000);
			}
			hexa.desasserv();
*/
/*			for(int j = 0; j < 100; j++)
			{
			    
                hexa.pattes[0][0].goto_etat(new EtatPatte(0, EtatPatteTest2.DEBOUT));
                hexa.pattes[0][2].goto_etat(new EtatPatte(2, EtatPatteTest2.DEBOUT));
                hexa.pattes[0][4].goto_etat(new EtatPatte(4, EtatPatteTest2.DEBOUT));

                hexa.pattes[0][1].goto_etat(new EtatPatte(1, EtatPatteTest2.POUSSE));
                hexa.pattes[0][3].goto_etat(new EtatPatte(3, EtatPatteTest2.POUSSE));
                hexa.pattes[0][5].goto_etat(new EtatPatte(5, EtatPatteTest2.POUSSE));

                Sleep.sleep(100);

                hexa.pattes[0][0].goto_etat(new EtatPatte(0, EtatPatteTest2.AVANT));
                hexa.pattes[0][2].goto_etat(new EtatPatte(2, EtatPatteTest2.AVANT));
                hexa.pattes[0][4].goto_etat(new EtatPatte(4, EtatPatteTest2.AVANT));

                hexa.pattes[0][1].goto_etat(new EtatPatte(1, EtatPatteTest2.ARRIERE));
                hexa.pattes[0][3].goto_etat(new EtatPatte(3, EtatPatteTest2.ARRIERE));
                hexa.pattes[0][5].goto_etat(new EtatPatte(5, EtatPatteTest2.ARRIERE));
                
                Sleep.sleep(50);

                hexa.pattes[0][1].goto_etat(new EtatPatte(1, EtatPatteTest2.DEBOUT));
                hexa.pattes[0][3].goto_etat(new EtatPatte(3, EtatPatteTest2.DEBOUT));
                hexa.pattes[0][5].goto_etat(new EtatPatte(5, EtatPatteTest2.DEBOUT));

                hexa.pattes[0][0].goto_etat(new EtatPatte(0, EtatPatteTest2.POUSSE));
                hexa.pattes[0][2].goto_etat(new EtatPatte(2, EtatPatteTest2.POUSSE));
                hexa.pattes[0][4].goto_etat(new EtatPatte(4, EtatPatteTest2.POUSSE));

                Sleep.sleep(100);

                hexa.pattes[0][1].goto_etat(new EtatPatte(1, EtatPatteTest2.AVANT));
                hexa.pattes[0][3].goto_etat(new EtatPatte(3, EtatPatteTest2.AVANT));
                hexa.pattes[0][5].goto_etat(new EtatPatte(5, EtatPatteTest2.AVANT));

                hexa.pattes[0][0].goto_etat(new EtatPatte(0, EtatPatteTest2.ARRIERE));
                hexa.pattes[0][2].goto_etat(new EtatPatte(2, EtatPatteTest2.ARRIERE));
                hexa.pattes[0][4].goto_etat(new EtatPatte(4, EtatPatteTest2.ARRIERE));
                
                Sleep.sleep(50);
			}*/
            //angle: [-60;10]
			
/*			System.out.println(1);
			hexa.goto_etat("000000");
			Sleep.sleep(1000);
            System.out.println(2);
            hexa.goto_etat("000001");
            Sleep.sleep(1000);
            System.out.println(3);
            hexa.goto_etat("000011");
            Sleep.sleep(1000);
            System.out.println(4);
            hexa.goto_etat("000111");
            Sleep.sleep(1000);
            System.out.println(5);
            hexa.goto_etat("001111");
            Sleep.sleep(1000);
            System.out.println(6);
            hexa.goto_etat("011111");
            Sleep.sleep(1000);
            System.out.println(7);
            hexa.goto_etat("111111");
            Sleep.sleep(1000);
            System.out.println(8);
            hexa.goto_etat("000000");
            Sleep.sleep(1000);
*/
/*            for(int j = 0; j < 3; j++)
			{
			for(int i = 0; i < 10; i++)
			{
			    System.out.println("+ "+i);
			    hexa.pattes[0][0].goto_etat(new EtatPatte(0., 100.-5*i, -100.));
			    Thread.sleep(100);
			}
            for(int i = 10; i > 0; i--)
            {
                System.out.println("- "+i);
                hexa.pattes[0][0].goto_etat(new EtatPatte(0., 100.-5*i, -100.));
                Thread.sleep(100);
            }
			}
            for(int j = 0; j < 3; j++)
            {
            for(int i = 0; i < 10; i++)
            {
                System.out.println("+ "+i);
                hexa.pattes[0][0].goto_etat(new EtatPatte(0., 100., -120.+5*i));
                Thread.sleep(100);
            }
            for(int i = 10; i > 0; i--)
            {
                System.out.println("- "+i);
                hexa.pattes[0][0].goto_etat(new EtatPatte(0., 100., -120.+5*i));
                Thread.sleep(100);
            }
            }*/
			scanner.close();
			serie.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

}
