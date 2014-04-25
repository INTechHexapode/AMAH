import hexapode.Hexapode;
import hexapode.markov.EtatPatte;

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
//			StandStillTest standstilltest = new StandStillTest(hexa, 100, 600., 0., true, true);
//			TestEngine testengine = new TestEngine(standstilltest);
//			testengine.start();
			for(int j = 0; j < 3; j++)
			{
			for(int i = 0; i < 10; i++)
			{
			    System.out.println("+ "+i);
			    hexa.pattes[0].goto_etat(new EtatPatte(0., 100.-5*i, -100.));
			    Thread.sleep(60);
			}
            for(int i = 10; i > 0; i--)
            {
                System.out.println("- "+i);
                hexa.pattes[0].goto_etat(new EtatPatte(0., 100.-5*i, -100.));
                Thread.sleep(60);
            }
			}
            for(int j = 0; j < 3; j++)
            {
            for(int i = 0; i < 10; i++)
            {
                System.out.println("+ "+i);
                hexa.pattes[0].goto_etat(new EtatPatte(0., 100., -120.+5*i));
                Thread.sleep(60);
            }
            for(int i = 10; i > 0; i--)
            {
                System.out.println("- "+i);
                hexa.pattes[0].goto_etat(new EtatPatte(0., 100., -120.+5*i));
                Thread.sleep(60);
            }
            }
			scanner.close();
			serie.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

}
