import hexapode.Deplacement;
import hexapode.Hexapode;
import hexapode.Vec2;
import hexapode.capteurs.Capteur;
import hexapode.capteurs.Sleep;
import hexapode.enums.Marche;
import hexapode.enums.Mode;
import hexapode.enums.Profil;
import hexapode.exceptions.BordureException;
import hexapode.exceptions.EnnemiException;
import hexapode.exceptions.GoToException;

import java.util.*;

import container.Container;
import scripts.Decision;
import serial.Serial;
import serial.SerialException;
import serial.SerialManager;
import test.*;

/*
 * TODO list (méca/élec)
 * Cartes électroniques (alim/capteurs)
 * Capteur 2 ultrason ou infrarouge + servo (on tourne le capteur dans la direction où on va)
 * Jumper
 * Bouton d'arrêt d'urgence
 * Raspberry
 * Lipo
 * Un ou deux boutons de manière à pouvoir le configurer sans pc?...
 * (un commutateur pour la couleur, un bouton poussoir pour le recalage)
 */

public class lanceur {

	public static void main(String[] args) {
		SerialManager serialmanager;
		Serial serie = null;
		Scanner scanner = null;
        Sleep.temps_defaut = 250;
		try {
		    serialmanager = new SerialManager();
			serie = serialmanager.getSerial("serieAsservissement");
        } catch (Exception e) {
            Sleep.temps_defaut = 0;
            System.out.println("Pas de série: "+e);
        }
		
		try {
		    Container container = new Container(serie, false, false);
    	    Deplacement deplacement = (Deplacement)container.getService("Deplacement");
            Hexapode hexa = (Hexapode)container.getService("Hexapode");
            Sleep sleep = (Sleep)container.getService("Sleep");
            Capteur capteur = (Capteur)container.getService("Capteur");
            if(serie != null)
            {
        		System.out.println("Attente");
        		scanner = new Scanner(System.in);
    		    scanner.nextLine();
            }
            if(serie == null)
                throw new Exception();

//            danse(deplacement, sleep);
            
            capteur.tourner(0);
            if(true)
            	return;
            
            Sleep.temps_defaut = 1500;
            hexa.avancer(300);
            Sleep.temps_defaut = 100;
            hexa.avancer(60);

            sleep.sleep(3000);
            Sleep.temps_defaut = 250;

            while(true)
            {
                hexa.setAngle(0);
                hexa.avancer(1000);
                hexa.setAngle(Math.PI/3);
                hexa.avancer(1000);
                hexa.setAngle(2*Math.PI/3);
                hexa.avancer(1000);
                hexa.setAngle(Math.PI);
                hexa.avancer(1000);
                hexa.setAngle(-2*Math.PI/3);
                hexa.avancer(1000);
                hexa.setAngle(-Math.PI/3);
                hexa.avancer(1000);
            }
            //lanceur_coupe(hexa);
            
            
//            TestCoordinationPattesSimulation test = new TestCoordinationPattesSimulation(deplacement, 1000000, 5000, 0, true, serie != null);
//            TestEngine testEngine = new TestEngine(test);
//            testEngine.start();

//          TestDeuxCoups test = new TestDeuxCoups(deplacement, 500000, 5000, 0, true, true);
//          TestEngine testEngine = new TestEngine(test);
//          testEngine.start();
          
//            deplacement.setMode(Mode.BIPHASE, Marche.BASIQUE);
//            hexa.avancer(100);
    //        hexa.va_au_point(new Vec2(-100, 600), true);
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		}
		if(serie != null)
		{
	        scanner.close();
		    serie.close();
		}
	}

	public static void test_validation(Deplacement deplacement)
	{
        TestCoordinationPattesSimulation test = new TestCoordinationPattesSimulation(deplacement, 10000, 50, 1, true, true);
        TestEngine testEngine = new TestEngine(test);
        testEngine.start();
	}
	
	public static void test_capteur(Serial serie)
	{
        for(int i = 0; i < 100; i++)
        {
            try
            {
                serie.communiquer("VD");
                System.out.println("Mesure: "+(int)(serie.readByte()));
            } catch (SerialException e)
            {
                e.printStackTrace();
            }
            try
            {
                Thread.sleep(500);
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

	}
	
	public static void lanceur_coupe(Hexapode hexa)
	{
        hexa.initialiser();
        try
        {
            hexa.va_au_point(new Vec2(1600, 800));
        } catch (EnnemiException | BordureException e)
        {
            e.printStackTrace();
        }
	}
	
	public static void danse(Deplacement deplacement, Sleep sleep)
	{
        Sleep.temps_defaut = 500;
	    
	    int[] tab = {0,1,2,5,4,3};
	    for(int i = 0; i < 8; i++)
	    {
	        for(int j = 0; j < 6; j++)
	        {
                int k = tab[j];
	            deplacement.lever(k);
	            sleep.sleep();
                deplacement.baisser((k+5)%6);
	        }
	    }
	}
	
}
